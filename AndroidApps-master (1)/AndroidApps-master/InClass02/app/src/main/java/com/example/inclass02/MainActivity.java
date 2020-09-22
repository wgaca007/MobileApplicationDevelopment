/**
 * a. Assignment #2
 * b. File Name.: inClass02
 * c. Full name of students of Group 20 : SANU DAS, AKHIL CHUNDARATHIL, RAVI THEJA GOALLA.
 */
package com.example.inclass02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv_shape;
    private TextView tv_result;
    private Button button_calculate;
    private Button button_clear;
    private ImageView iv_triangle;
    private ImageView iv_circle;
    private ImageView iv_square;
    private EditText et_length1;
    private EditText et_length2;
    private String selectedShape = "";
    double length1;
    double length2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Area Calculator");   //App Name

        et_length1 = findViewById(R.id.et_length1);
        et_length2 = findViewById(R.id.et_length2);
        tv_result = findViewById(R.id.tv_result);
        tv_shape = findViewById(R.id.tv_shape);
        button_calculate = findViewById(R.id.button_calculate);
        button_clear = findViewById(R.id.button_clear);
        iv_triangle = findViewById(R.id.iv_triangle);
        iv_square = findViewById(R.id.iv_square);
        iv_circle = findViewById(R.id.iv_circle);

        clearButton();

        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearButton();
            }
        });

        View.OnClickListener listener= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(v == iv_triangle){
                 selectedShape = "triangle";
                 tv_shape.setText("Triangle");
                 et_length2.setVisibility(View.VISIBLE);
             }
             if(v == iv_square){
                 selectedShape = "square";
                 tv_shape.setText("Square");
                 et_length2.setVisibility(View.INVISIBLE);
             }
             if(v == iv_circle){
                 selectedShape = "circle";
                 tv_shape.setText("Circle");
                 et_length2.setVisibility(View.INVISIBLE);
             }
             tv_shape.setError(null);
             tv_result.setText("");
            }
        };

        iv_triangle.setOnClickListener(listener);
        iv_square.setOnClickListener(listener);
        iv_circle.setOnClickListener(listener);

        button_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validation = false;
                if (tv_shape.getText().equals("Select a Shape")) {
                    tv_shape.setError("");
                    return;
                }
                if(et_length1.getText().toString().equals("")) {
                    et_length1.setError("Hey I need a value");
                    validation = true;
                }
                if(selectedShape.equals("triangle") && et_length2.getText().toString().equals("")) {
                    et_length2.setError("Hey I need a value");
                    validation = true;
                }
                if (validation) {
                    tv_result.setText("");
                    return;
                }
                String tempLength1 = et_length1.getText().toString();
                String tempLength2 = et_length2.getText().toString();

                if (tempLength1!=null && !tempLength1.equals(""))
                    length1 = Double.parseDouble(tempLength1);
                if (tempLength2!=null && !tempLength2.equals(""))
                    length2 = Double.parseDouble(tempLength2);

                double result;

                switch (selectedShape) {
                    case "triangle":
                        result = 0.5*length1*length2;
                        tv_result.setText(result+"");
                        break;
                    case "square":
                        result = length1*length1;
                        tv_result.setText(result+"");
                        break;
                    case "circle":
                        result = Math.PI*length1*length1;
                        tv_result.setText(result+"");
                        break;

                }
            }
        });
    }

    public void clearButton() {
        et_length1.setVisibility(View.VISIBLE);
        et_length1.setText("");
        et_length1.setError(null);
        et_length2.setVisibility(View.VISIBLE);
        et_length2.setText("");
        et_length2.setError(null);
        tv_shape.setText("Select a Shape");
        tv_result.setText("");
        tv_shape.setError(null);

    }
}
