package com.eebbk.gbofsafetyknowledge.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.eebbk.gbofsafetyknowledge.activity.AnswerActivity;
import com.eebbk.gbofsafetyknowledge.beans.QuestionVO;
import com.eebbk.gbofsafetyknowledge.contants.Contants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * decription ：
 * author ： zhua
 */
public class QuestionDAO {

    private static final String TAG = "QuestionDAO";
    private final String DB_NAME = "questions.db";
    private final Context mContext;
    private String mDatabasePath;
    private SQLiteDatabase mQuestionsDb;
    private final SharedPreferences mSharedPreferences; //私有数据

    public QuestionDAO(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(Contants.PREFERENCENAME, Context.MODE_PRIVATE);
    }

    /**
     * description:将数据库从assets 文件夹复制到手机自身的存储当中去
     * author:zhua
     */
    private void copyDatabase() {
        String databasePath = mContext.getFilesDir().getAbsolutePath();
        databasePath = databasePath.substring(0, databasePath.lastIndexOf("/")) + "/databases";
        mDatabasePath = databasePath + "/" + DB_NAME;

        File dir = new File(databasePath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(mDatabasePath);

        if (file.exists()) {//如果数据库存在判断是不是重新安装apk，如果是则将旧的删掉，新的加入
            if (!mSharedPreferences.getBoolean("isFirst", false)) {
                file.delete();
            }
        }

        if (!file.exists() || file.length() == 0) {
            try {
                InputStream is = null;
                try {
                    is = mContext.getAssets().open("database/" + DB_NAME);
                    if(is != null){
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileOutputStream fos = new FileOutputStream(mDatabasePath);
                byte[] buffer = new byte[1024];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
                //获取编辑器
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putBoolean("isFirst", true);
                //提交修改
                editor.apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * description:打开数据库
     * author:zhua
     */
    private void openDatabase() {
        Log.v(TAG, "Open DataBase : " + mDatabasePath);
        mQuestionsDb = SQLiteDatabase.openDatabase(mDatabasePath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
    }

    /**
     * description:关闭数据库
     * author:zhua
     */
    private void closeDatabase() {
        SQLiteDatabase.releaseMemory();
        mQuestionsDb.close();
    }

    /**
     * description:获取对应的题目
     * author:zhua
     */
    private List<QuestionVO> getQuestions(int grade) {//  0  学前   1  小学

        String sql = "select * from QUESTIONS where GRADE = ?";
        openDatabase();

        String str_grade = grade + "";
        List<QuestionVO> listQuestionVOFinally = new ArrayList<>();
        List<QuestionVO> listQuestionVOProduct = new ArrayList<>();
        List<QuestionVO> listQuestionVOPure = new ArrayList<>();

        Cursor cursor = mQuestionsDb.rawQuery(sql, new String[]{str_grade});
        if (cursor == null) {
            Log.e(TAG, "Cursor is null.");
        } else {
            while (cursor.moveToNext()) {
                QuestionVO questionVO = new QuestionVO();
                int titleColumn = cursor.getColumnIndex("TITLE");
                int contentAColumn = cursor.getColumnIndex("OPTIONA");
                int contentBColumn = cursor.getColumnIndex("OPTIONB");
                int contentCColumn = cursor.getColumnIndex("OPTIONC");
                int contentDColumn = cursor.getColumnIndex("OPTIOND");
                int answerColumn = cursor.getColumnIndex("ANSWER");
                int formatColumn = cursor.getColumnIndex("QUESTION_FORMAT");
                int typeColumn = cursor.getColumnIndex("QUESTION_TYPE");
                int picIDColumn = cursor.getColumnIndex("PICID");
                int voiceIDColumn = cursor.getColumnIndex("VOICEID");
                int extendColumn = cursor.getColumnIndex("EXTEND");

                questionVO.setmTitle(cursor.getString(titleColumn));
                questionVO.setmOptionA(cursor.getString(contentAColumn));
                questionVO.setmOptionB(cursor.getString(contentBColumn));
                questionVO.setmOptionC(cursor.getString(contentCColumn));
                questionVO.setmOptionD(cursor.getString(contentDColumn));
                questionVO.setmAnswer(cursor.getString(answerColumn));
                questionVO.setmQuestionFormat(cursor.getInt(formatColumn));
                questionVO.setmQuestionType(cursor.getInt(typeColumn));
                questionVO.setmPicID(cursor.getString(picIDColumn));
                questionVO.setmVoiceID(cursor.getString(voiceIDColumn));
                questionVO.setmExtend(cursor.getString(extendColumn));

                if (questionVO.getmQuestionType() == 1) {
                    listQuestionVOPure.add(questionVO);
                } else if (questionVO.getmQuestionType() == 2) {
                    listQuestionVOProduct.add(questionVO);
                }
            }
        }

        if(cursor != null){
            cursor.close();
        }
        closeDatabase();

        if (!listQuestionVOProduct.isEmpty()) {
            listQuestionVOFinally.addAll(randomTopic(listQuestionVOProduct, 2));
        }
        if (!listQuestionVOPure.isEmpty()) {
            listQuestionVOFinally.addAll(randomTopic(listQuestionVOPure, 4));
        }
        // randomSortList(listQuestionVOFinally);

        return listQuestionVOFinally;
    }

    /**
     * 从List中随机出count个对象
     */
    private List<QuestionVO> randomTopic(List<QuestionVO> list, int count) {
        // 创建一个长度为count(count<=list)的数组,用于存随机数
        int[] a = new int[count];
        // 利于此数组产生随机数
        int[] b = new int[list.size()];
        int size = list.size();

        // 取样填充至数组a满
        for (int i = 0; i < count; i++) {
            int num = (int) (Math.random() * size); // [0,size)
            int where = -1;
            for (int j = 0; j < b.length; j++) {
                if (b[j] != -1) {
                    where++;
                    if (where == num) {
                        b[j] = -1;
                        a[i] = j;
                    }
                }
            }
            size = size - 1;
        }
        // a填满后 将数据加载到rslist
        List<QuestionVO> rslist = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            QuestionVO df = list.get(a[i]);
            rslist.add(df);
        }
        return rslist;
    }

    /**
     * des:对list进行随机排序
     */
    public void randomSortList(List<QuestionVO> listT) {
        Random random = new Random();
        List<QuestionVO> newList = new ArrayList<>();

        for (QuestionVO item : listT) {
            newList.add(random.nextInt(newList.size()), item);
        }

        listT.clear();
        listT.addAll(newList);
    }

    public void selectQuestion(int grade, Handler handler) {
        copyDatabase();
        List<QuestionVO> questionVOs = getQuestions(grade);

        Message msg = handler.obtainMessage();
        msg.obj = questionVOs;
        msg.what = AnswerActivity.SELECT_OVER;
        msg.sendToTarget();
    }
}