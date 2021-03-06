package org.expertprogramming.taskplaceremainder.Activities;

/**
 * Created by pramod on 11/1/18.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.expertprogramming.taskplaceremainder.Helper.DialogHelper;
import org.expertprogramming.taskplaceremainder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class SignupActivity extends AppCompatActivity {
    FirebaseAuth auth;
    EditText email,password;
    Button signup;
    TextView itoLogin;
    TextInputLayout emailWrap,passWrap;
    DialogHelper dialogHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogHelper=new DialogHelper(SignupActivity.this);
        setContentView(R.layout.activity_signup);
        auth = FirebaseAuth.getInstance();
        itoLogin=findViewById(R.id.itologin);
        emailWrap=findViewById(R.id.emailWrapper);
        passWrap=findViewById(R.id.passwordWrapper);
        emailWrap.setHint("Email ID");
        passWrap.setHint("Password");
        email=findViewById(R.id.editText1);
        password=findViewById(R.id.editText2);
        signup=findViewById(R.id.button);
        itoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogHelper.showProgressDialog("Please wait......");
                String email_id=email.getText().toString();
                String password_=password.getText().toString();
                if(TextUtils.isEmpty(email_id)){
                    Toasty.warning(getApplicationContext(),"Please enter your email id",Toast.LENGTH_SHORT).show();
                    dialogHelper.dismissProgressDialog();
                    return;
                }
                if(TextUtils.isEmpty(password_)){
                    Toasty.warning(getApplicationContext(),"Please enter password",Toast.LENGTH_SHORT).show();
                    dialogHelper.dismissProgressDialog();
                    return;
                }
                if(password_.length()<6){
                    Toasty.warning(getApplicationContext(),"Password to short",Toast.LENGTH_SHORT).show();
                    dialogHelper.dismissProgressDialog();
                    return;

                }
                auth.createUserWithEmailAndPassword(email_id,password_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.sendEmailVerification();
                            Toasty.success(getApplicationContext(),"We have sent you a mail for verification",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String error=task.getException().getMessage().toString();
                            Toasty.error(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

                        }

                        dialogHelper.dismissProgressDialog();

                    }
                });

            }
        });

    }
    protected void onStop(){
        super.onStop();
        finish();
    }


}

