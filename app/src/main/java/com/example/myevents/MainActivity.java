package com.example.myevents;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myevents.models.Actions;
import com.example.myevents.models.Belong;
import com.example.myevents.models.Belongs;
import com.example.myevents.models.Groups;
import com.example.myevents.models.Member;
import com.example.myevents.models.Members;
import com.example.myevents.models.Notes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "PhoneAuthActivity";

    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    EditText phoneNumberInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        isLoggedin();

        phoneNumberInput = findViewById(R.id.phone_number_input);
        EditText codeInput = findViewById(R.id.code_input);
        Button sendCodeButton = findViewById(R.id.send_code_button);
        Button verifyCodeButton = findViewById(R.id.verify_code_button);
        Button resendCodeButton = findViewById(R.id.resend_code_button);

        // Initialize phone auth callbacks
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(MainActivity.this, "Invalid phone number.", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(MainActivity.this, "SMS quota exceeded.", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                    Toast.makeText(MainActivity.this, "reCAPTCHA verification failed.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberInput.getText().toString();
                startPhoneNumberVerification(phoneNumber);
            }
        });

        verifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeInput.getText().toString();
                verifyPhoneNumberWithCode(mVerificationId, code);
            }
        });

        resendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberInput.getText().toString();
                resendVerificationCode(phoneNumber, mResendToken);
            }
        });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        if(code.equals(""))
        {
            Toast.makeText(this, "Please insert verification code!", Toast.LENGTH_SHORT).show();
            return;
        }
        if( verificationId==null)
        {
            Toast.makeText(this, "You have to ask for verification code!", Toast.LENGTH_SHORT).show();
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(MainActivity.this, "Invalid verification code.", Toast.LENGTH_SHORT).show();
                            }
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(this, "Authentication successful!", Toast.LENGTH_SHORT).show();


            getName();

        } else {
            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
        }


    }


    void getName() {

        String phone =mAuth.getCurrentUser().getPhoneNumber();
        FirebaseHelper.fetchData("members", phone, Member.class, new FirebaseHelper.DataCallbackSpecific<Member>() {
            @Override
            public void onSuccess(Member data) {
                System.out.println("Data fetched successfully: " + data);
                Log.e("info", data.getName());

                move();
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println("Error fetching data: " + e.getMessage());
                enterName();
            }
        });

    }


    void enterName() {
        LinearLayout lLEnterFirstName = findViewById(R.id.lLEnterFirstName);
        lLEnterFirstName.setVisibility(View.VISIBLE);

        Button btnEnterFirstName = findViewById(R.id.btnEnterFirstName);

        btnEnterFirstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                EditText eTEnterNameFirstTime = findViewById(R.id.eTEnterNameFirstTime);
                String name = eTEnterNameFirstTime.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Empty name field!", Toast.LENGTH_SHORT).show();
                    return;
                }


                FirebaseHelper firestoreHelper = new FirebaseHelper();

                String phone = mAuth.getCurrentUser().getPhoneNumber();
                Map<String, Object> data = new HashMap<>();
                data.put("name", name);
                data.put("phone", phone);

                String customDocumentId = mAuth.getCurrentUser().getPhoneNumber();

                firestoreHelper.setDocument("members", customDocumentId, data);

                move();

            }
        });

    }

    public void isLoggedin() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            getName();
        }
    }

    void move() {



        Members.getInstance().clear();
        Notes.getInstance().clear();
        Belongs.getInstance().clear();
        Groups.getInstance().clear();
        Actions.getInstance().clear();

        String phone = mAuth.getCurrentUser().getPhoneNumber();
        Member.setCurrent(phone);

        finish();
        startActivity(new Intent(MainActivity.this, MainMenuActivity.class));

    }
}
