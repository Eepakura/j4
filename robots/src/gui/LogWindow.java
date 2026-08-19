package gui;

import java.awt.*;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindow extends JInternalFrame implements LogChangeListener, LocaleChangeListener
{
    private LogWindowSource mLogSource;
    private TextArea mLogContent;

    private static final int widthSize = 200;
    private static final int heightSize = 500;

    public LogWindow(LogWindowSource logSource)
    {
        super(Localization.getValue("protocolWork"), true, true, true, true);

        mLogSource = logSource;
        mLogSource.registerListener(this);
        mLogContent = new TextArea("");
        mLogContent.setSize(widthSize, heightSize);
        mLogContent.setEditable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(mLogContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        setResizable(true);
        updateLogContent();
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : mLogSource.all())
        {
            content.append(Localization.getValue(entry.getMessage())).append("\n");
        }
        mLogContent.setText(content.toString());
        mLogContent.invalidate();
    }

    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public void onLocaleChange() {
        this.setTitle(Localization.getValue("protocolWork"));
        onLogChanged();
    }
}
