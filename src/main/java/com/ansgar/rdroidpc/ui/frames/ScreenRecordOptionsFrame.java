package com.ansgar.rdroidpc.ui.frames;

import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.entities.ScreenRecordOptions;
import com.ansgar.rdroidpc.listeners.OnFileChooserListener;
import com.ansgar.rdroidpc.listeners.OnScreenRecordOptionsListener;
import com.ansgar.rdroidpc.ui.components.FileChooser;
import com.ansgar.rdroidpc.utils.KeyListenerAdapter;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ScreenRecordOptionsFrame extends BasePanel implements OnFileChooserListener {

    private OnScreenRecordOptionsListener listener;
    private int screenRecordWidth = DimensionConst.DEFAULT_WIDTH;
    private int screenRecordHeight = DimensionConst.DEFAULT_HEIGHT;
    private JTextField downloadFolderTf, bitRateFtf, timeFtf;

    public ScreenRecordOptionsFrame(Rectangle rectangle, String title) {
        super(rectangle, title);

        addActionButtons();
        addScreenSize();
        addBitRate();
        addTime();
        addDownloadFolder();
        setLayout(new BorderLayout());
    }

    private void addActionButtons() {
        JButton okBtn = new JButton(StringConst.OK);
        okBtn.setBounds(getRectangle().width - 230, getRectangle().height - 95, 100, 50);
        okBtn.setFocusable(false);
        okBtn.addActionListener(e -> {
            ScreenRecordOptions options = createScreenOptions();
            if (listener != null && options != null) {
                listener.onOptionsSelected(options);
            } else {

            }
            closeFrame();
        });
        add(okBtn);

        JButton cancelBtn = new JButton(StringConst.CANCEL);
        cancelBtn.setBounds(getRectangle().width - 120, getRectangle().height - 95, 100, 50);
        cancelBtn.setFocusable(false);
        cancelBtn.addActionListener(e -> closeFrame());
        add(cancelBtn);
    }

    private void addScreenSize() {
        JLabel screenSize = new JLabel(StringConst.SCREEN_RESOLUTION_L);
        screenSize.setBounds(5, 5, 200, 25);
        add(screenSize);

        String[] screenSizes = StringConst.Companion.getDefaultScreenSizes();
        JComboBox screenResolutionCb = new JComboBox<>(screenSizes);
        screenResolutionCb.setBounds(getRectangle().width - 230, 5, 205, 25);
        screenResolutionCb.addActionListener(e -> {
            int index = screenResolutionCb.getSelectedIndex();
            String[] sizes = screenSizes[index].split("x");
            screenRecordWidth = Integer.valueOf(sizes[0]);
            screenRecordHeight = Integer.valueOf(sizes[1]);
        });
        screenResolutionCb.setSelectedIndex(2);
        add(screenResolutionCb);
    }

    private void addBitRate() {
        JLabel bitRateL = new JLabel(StringConst.BIT_RATE_L);
        bitRateL.setBounds(5, 35, 200, 25);
        add(bitRateL);

        bitRateFtf = new JTextField();
        bitRateFtf.setText("4");
        bitRateFtf.setColumns(2);
        bitRateFtf.addKeyListener(getKeyListener());
        bitRateFtf.setBorder(new MatteBorder(1, 1, 1, 1, Color.WHITE));
        bitRateFtf.setBounds(getRectangle().width - 230, 35, 205, 25);
        add(bitRateFtf);
    }

    private void addTime() {
        JLabel timeL = new JLabel(StringConst.TIME_L);
        timeL.setBounds(5, 65, 200, 25);
        add(timeL);

        timeFtf = new JTextField();
        timeFtf.setText("180");
        timeFtf.setBorder(new MatteBorder(1, 1, 1, 1, Color.WHITE));
        timeFtf.setColumns(3);
        timeFtf.addKeyListener(getKeyListener());
        timeFtf.setBounds(getRectangle().width - 230, 65, 205, 25);
        add(timeFtf);
    }

    private void addDownloadFolder() {
        JLabel downloadFolderL = new JLabel(StringConst.DOWNLOAD_FOLDER);
        downloadFolderL.setBounds(5, 95, 200, 25);
        add(downloadFolderL);

        downloadFolderTf = new JTextField();
        downloadFolderTf.setBorder(new MatteBorder(1, 1, 1, 1, Color.WHITE));
        downloadFolderTf.setBounds(getRectangle().width - 230, 95, 185, 25);
        add(downloadFolderTf);

        JToggleButton toggleBtn = new JToggleButton("...");
        toggleBtn.setFocusable(false);
        toggleBtn.setBounds(getRectangle().width - 230 + 185, 95, 20, 25);
        toggleBtn.addActionListener(e -> {
            FileChooser fileChooser = new FileChooser(this);
            fileChooser.open(this, JFileChooser.FILES_AND_DIRECTORIES);
        });

        add(toggleBtn);
    }

    private KeyListenerAdapter getKeyListener() {
        return new KeyListenerAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char eChar = e.getKeyChar();
                if ((!(Character.isDigit(eChar)
                        || (eChar == KeyEvent.VK_BACK_SPACE)
                        || (eChar == KeyEvent.VK_DELETE)))) {
                    e.consume();
                }
            }
        };
    }

    @Nullable
    private ScreenRecordOptions createScreenOptions() {
        if (bitRateFtf.getText().isEmpty()) {

        } else if (timeFtf.getText().isEmpty()) {

        } else if (downloadFolderTf.getText().isEmpty()) {

        } else {
            return new ScreenRecordOptions(
                    screenRecordWidth,
                    screenRecordHeight,
                    Integer.valueOf(bitRateFtf.getText()),
                    Integer.valueOf(timeFtf.getText()),
                    downloadFolderTf.getText()
            );
        }
        return null;
    }

    public void setListener(OnScreenRecordOptionsListener listener) {
        this.listener = listener;
    }

    @Override
    public void onPathSelected(String path) {
        downloadFolderTf.setText(path);
    }
}
