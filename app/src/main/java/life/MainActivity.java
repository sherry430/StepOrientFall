package life;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import life.orient.OrientSensor;
import life.step.StepSensorAcceleration;
import life.step.StepSensorBase;
import life.util.SensorUtil;

public class MainActivity extends AppCompatActivity implements StepSensorBase.StepCallBack, OrientSensor.OrientCallBack {
    private TextView mStepText;
    private TextView mOrientText;
    private StepView mStepView;
    private StepSensorBase mStepSensor; // 计步传感器
    private OrientSensor mOrientSensor; // 方向传感器
//    private GyroSensor mGyroSensor; // 方向传感器
    private int mStepLen = 50; // 步长

    @Override
    public void Step(int stepNum, int fallNum, int CURRENT_FALL) {
        //  计步回调
        mStepText.setText("步数:" + stepNum + "\n摔倒次数:" + fallNum);
        mStepView.autoAddPoint(mStepLen, CURRENT_FALL);
    }

    @Override
    public void Orient(int orient) {
        // 方向回调
        mOrientText.setText("方向:" + orient);
//        获取手机转动停止后的方向
//        orient = SensorUtil.getInstance().getRotateEndOrient(orient);
        mStepView.autoDrawArrow(orient);
    }

//    @Override
//    public void Gyro(int gyro) {
//        //陀螺仪回调
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SensorUtil.getInstance().printAllSensor(this); // 打印所有可用传感器
        setContentView(R.layout.activity_main);
        mStepText = (TextView) findViewById(R.id.step_text);
        mOrientText = (TextView) findViewById(R.id.orient_text);
        mStepView = (StepView) findViewById(R.id.step_surfaceView);
        // 选择使用哪个计步
//        mStepSensor = new StepSensorPedometer(this, this);  //计步传感器来计步
        mStepSensor = new StepSensorAcceleration(this, this);  //加速度 计步
        if (!mStepSensor.registerStep()) {
            Toast.makeText(this, "计步功能不可用！", Toast.LENGTH_SHORT).show();
        }

        // 注册方向监听
        mOrientSensor = new OrientSensor(this, this);
        if (!mOrientSensor.registerOrient()) {
            Toast.makeText(this, "方向功能不可用！", Toast.LENGTH_SHORT).show();
        }

//        mGyroSensor = new GyroSensor(this, this);
//        if (!mGyroSensor.registerGyro()) {
//            Toast.makeText(this, "方向功能不可用！", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销传感器监听
        mStepSensor.unregisterStep();
        mOrientSensor.unregisterOrient();
//        mGyroSensor.unregisterGyro();
    }
}
