package com.example.triviatest.data;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.triviatest.controller.AppController;
import com.example.triviatest.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;


public class QuestionBank {
    ArrayList<Question> questionArrayList = new ArrayList<>();
    private String url="https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestion(final AnswerListAsyncResponse callBack){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, (JSONArray) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for( int i=0; i<response.length(); i++){
                    try {
                        Question question= new Question();

                        question.setAnswer(response.getJSONArray(i).get(0).toString());
                        question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));
                        //response.getJSONArray(i).get(0);
                        //response.getJSONArray(i).getBoolean(1)
                        questionArrayList.add(question);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //Log.d("json", "onResponse: "+ response);
                if (null!= callBack) callBack.processFinished(questionArrayList);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionArrayList;
    }

}
