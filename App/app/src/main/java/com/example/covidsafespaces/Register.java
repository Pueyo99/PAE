package com.example.covidsafespaces;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class Register extends AppCompatActivity {

    private TextView username;
    private TextView password1;
    private TextView password2;
    private Button registerButton;
    private boolean password1Visible = false;
    private boolean password2Visible = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.registerUsername);
        password1 = findViewById(R.id.registerPassword);
        password2 = findViewById(R.id.registerpassword2);
        registerButton = findViewById(R.id.registerButton);

        password1.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (password1.getRight() - password1.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        showHidePassword1();
                        return true;
                    }
                }
                return false;
            }
        });

        password2.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (password2.getRight() - password2.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        showHidePassword2();
                        return true;
                    }
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });


    }

    private void register(){
        if(password1.getText().toString().trim().equals(password2.getText().toString().trim())){
            new ServerConnection().register(username.getText().toString().trim(), password1.getText().toString().trim());
            startActivity(new Intent(Register.this, Main.class));
        } else{
            password1.setText("");
            password2.setText("");
            showMessage();
        }
    }

    private void showHidePassword1(){
        if(password1Visible){
            password1.setTransformationMethod(PasswordTransformationMethod.getInstance());

            password1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_visible, 0);
            password1Visible = false;
        } else{
            password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            password1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_novisible, 0);
            password1Visible = true;
        }
    }

    private void showHidePassword2(){
        if(password2Visible){
            password2.setTransformationMethod(PasswordTransformationMethod.getInstance());

            password2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_visible, 0);
            password2Visible = false;
        } else{
            password2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            password2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_novisible, 0);
            password2Visible = true;
        }
    }

    public void showMessage(){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View v = inflater.inflate(R.layout.show_capacity, null,false);

        ((TextView)v.findViewById(R.id.capacidad)).setText("Las contraseñas introducidas no son iguales");

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(v,width,height,true);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(v, Gravity.CENTER, 0 ,0);
            }
        });

    }

}