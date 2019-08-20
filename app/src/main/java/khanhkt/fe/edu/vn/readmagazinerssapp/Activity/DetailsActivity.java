package khanhkt.fe.edu.vn.readmagazinerssapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import khanhkt.fe.edu.vn.readmagazinerssapp.R;

public class DetailsActivity extends AppCompatActivity {

    //define position to process
    private int feedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //get Intent to process
        Intent processIntent = getIntent();
        //get position of selected Feed
        feedPosition = processIntent.getIntExtra("position", -1);
        //if content is transfer from main to details
        if (feedPosition != -1) {
            //get all control on form
            TextView lblTitle = (TextView)findViewById(R.id.lblTitle);
            TextView lblDescription = (TextView)findViewById(R.id.tblDescription);
            TextView lblDate = (TextView)findViewById(R.id.lblDate);
            //show information
            lblTitle.setText(processIntent.getStringExtra("title"));
            lblDescription.setText(processIntent.getStringExtra("description"));
            lblDate.setText(processIntent.getStringExtra("date"));
        }//end if feedPostion different -1
    }
}
