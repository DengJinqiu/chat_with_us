package jinqiu.chat.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import jinqiu.chat.R;
import jinqiu.chat.controller.ApplicationServer;
import jinqiu.chat.model.BackendServer;
import jinqiu.chat.view.message.MessageManager;

public class ChatPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_panel);

        // Simulate the backend server
        BackendServer backendServer = new BackendServer();

        ApplicationServer applicationServer = new ApplicationServer(applicationServerId);

        applicationServer.registerBackendServer(backendServer);


    }

    private MessageManager messageManager;

    // Dummy application server id
    private static final Integer applicationServerId = 1;
}