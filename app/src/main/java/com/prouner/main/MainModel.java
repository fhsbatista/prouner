package com.prouner.main;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.prouner.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainModel implements MainMVP.Model {

    /**
     * Callback that can be used to notify that the question's download has been completed
     */
    private OnQuestionRequestListener onQuestionRequestListener;

    @Override
    public void getQuestionAttributesArray() {
        final List<Question> questionList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("questions").child("sentences");


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    final String[] options = new String[4];
                    options[0] = data.child("option1").getValue(String.class);
                    options[1] = data.child("option2").getValue(String.class);
                    options[2] = data.child("option3").getValue(String.class);
                    options[3] = data.child("option4").getValue(String.class);
                    String urlFile = data.child("urlFile").getValue(String.class);
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference urlReferenceStorage = storage.getReferenceFromUrl(urlFile);

                    final long ONE_MEGABYTE = 1024 * 1024;
                    urlReferenceStorage.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Question question = new Question(bytes, options);
                            questionList.add(question);
                            Collections.shuffle(questionList);
                            if (onQuestionRequestListener != null) {
                                onQuestionRequestListener.onSuccess(questionList);
                            }

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (onQuestionRequestListener != null) {
                    onQuestionRequestListener.onError();
                }
            }
        });
    }

    /**
     * Setter for the field <b>onQuestionRequestListener</b>, which is a listener required by the MainModel
     * in order to notifies the question's download's results.
     * @param onQuestionRequestListener A listener that has to implement onSuccess and onError methods.
     */
    public void setOnQuestionRequestListener(OnQuestionRequestListener onQuestionRequestListener) {
        this.onQuestionRequestListener = onQuestionRequestListener;
    }

    public interface OnQuestionRequestListener {

        /**
         * Notifies that the question's download has been completed successfully.
         * @param questionsList List of questions which has been downloaded.
         */
        void onSuccess(List<Question> questionsList);

        /**
         * Notifies that the question's download <b>hasn't</b> been completed successfully.
         */
        void onError();
    }
}
