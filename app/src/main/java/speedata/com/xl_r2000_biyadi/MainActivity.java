package speedata.com.xl_r2000_biyadi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.speedata.libuhf.XinLianQilian;

public class MainActivity extends AppCompatActivity {

    private XinLianQilian xinLianQilian;
    private Button btn_read,btn_write,btn_clean;
    private TextView tv_show_read;
    private EditText et_show_write;//et_address,et_count,

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
    }

    @Override
    protected void onPause() {
        super.onPause();
        xinLianQilian.CloseDev();
    }

    @Override
    protected void onResume() {
        super.onResume();
        OpenDev();
    }

    private void initUi() {
        btn_read= (Button) findViewById(R.id.btn_read);
        btn_write= (Button) findViewById(R.id.btn_write);
        btn_clean= (Button) findViewById(R.id.cleanBtn);
        btn_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = xinLianQilian.write_area(3, "0", "0", "4", "00 00 00 00 00 00 00 00 ");
                if (i==0){
                    Toast.makeText(MainActivity.this,"清空成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,"清空失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_show_read= (TextView) findViewById(R.id.tv_show_read);
        et_show_write= (EditText) findViewById(R.id.et_show_write);

        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_show_read.setText("");
                byte[] rdata = xinLianQilian.read_area(3, 0, 4, 0);
                if (rdata==null){
                    Toast.makeText(MainActivity.this,"读取失败",Toast.LENGTH_SHORT).show();
                    return;
                }
                char[] out=null;
                out=new char[rdata.length];
                for(int i=0;i<rdata.length;i++)
                    out[i]=(char) rdata[i];
                if (out!=null){
                    Toast.makeText(MainActivity.this,"读取成功",Toast.LENGTH_SHORT).show();
                }else {
                    return;
                }
                String val=String.valueOf(out);
                tv_show_read.setText(val);
            }
        });


        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] data=null;
                String ascstr=et_show_write.getText().toString();
                if(ascstr.length()%2!=0)
                    ascstr+="0";
                data=ascstr.getBytes();
                int i = xinLianQilian.write_area(3, 0, 0, data);
                if (i==0){
                    Toast.makeText(MainActivity.this,"写入成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,"写入失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void OpenDev() {
        xinLianQilian = new XinLianQilian(this);
        int i = xinLianQilian.OpenDev();
        if (i==0){
            Toast.makeText(this,"初始化成功",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"初始化失败",Toast.LENGTH_SHORT).show();
            return;
        }
        xinLianQilian.set_antenna_power(30);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
