package jinqiu.chat.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;

import jinqiu.chat.R;
import jinqiu.chat.controller.ApplicationServer;
import jinqiu.chat.model.BackendServer;
import jinqiu.chat.view.message.MessageView;

public class ChatPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_panel);

        // Simulate the backend server
        BackendServer backendServer = new BackendServer();

        ApplicationServer applicationServer = new ApplicationServer(applicationServerId);

        applicationServer.registerBackendServer(backendServer);





        ViewGroup messageContainer = (ViewGroup) this.findViewById(R.id.message_container);


        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        params.leftMargin = 0; //Your X coordinate
        params.topMargin = 0; //Your Y coordinate

        MessageView mv = new MessageView(this);
        mv.setBackgroundColor(0xFF00FF00);
        messageContainer.addView(mv, params);
    }

    // Dummy application server id
    private static final Integer applicationServerId = 1;
}