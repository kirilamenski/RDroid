package com.ansgar.rdroidpc.ui.frames;

import com.ansgar.rdroidpc.constants.DimensionConst;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.entities.ScreenRecordOptions;
import com.ansgar.rdroidpc.listeners.OnFileChooserListener;
import com.ansgar.rdroidpc.listeners.OnScreenRecordOptionsListener;
import com.ansgar.rdroidpc.ui.components.FileChooser;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class ScreenRecordOptionsFrame extends BasePanel implements OnFileChooserListener {

    private OnScreenRecordOptionsListener listener;
    private int screenRecordWidth = DimensionConst.DEFAULT_WIDTH;
    private int screenRecordHeight = DimensionConst.DEFAULT_HEIGHT;
    private int bitRate = 4;
    private int time = 180;
    private JTextField downloadFolderTf;

    public ScreenRecordOptionsFrame(Rectangle rectangle, String title) {
        super(rectangle, title);

        addLabels();
        addComboBoxes();
        addActionButtons();
        addTextFields();
        addToggleBoxes();
    }

    private void addLabels() {
        int margin = 5;
        int height = 25;
        int width = 150;
        JLabel screenSize = new JLabel(StringConst.SCREEN_RESOLUTION_L);
        screenSize.setBounds(margin, margin, width, height);
        add(screenSize);

        JLabel bitRateL = new JLabel(StringConst.BIT_RATE_L);
        bitRateL.setBounds(margin, height + margin * 2, width, height);
        add(bitRateL);

        JLabel timeL = new JLabel(StringConst.TIME_L);
        timeL.setBounds(margin, (height + margin) * 2, width, height);
        add(timeL);

        JLabel downloadFolderL = new JLabel(StringConst.DOWNLOAD_FOLDER);
        downloadFolderL.setBounds(margin, (height + margin) * 2 + margin + height, width, height);
        add(downloadFolderL);
    }

    private void addComboBoxes() {
        int marginLeft = 155;
        int margin = 5;
        int width = 100;
        int height = 25;

        String[] screenSizes = StringConst.Companion.getDefaultScreenSizes();
        JComboBox screenResolutionCb = new JComboBox<>(screenSizes);
        screenResolutionCb.setBounds(marginLeft, margin, width, height);
        screenResolutionCb.addActionListener(e -> {
            int index = screenResolutionCb.getSelectedIndex();
            String[] sizes = screenSizes[index].split("x");
            screenRecordWidth = Integer.valueOf(sizes[0]);
            screenRecordHeight = Integer.valueOf(sizes[1]);
        });
        screenResolutionCb.setSelectedIndex(2);
        add(screenResolutionCb);
    }

    private void addTextFields() {
        downloadFolderTf = new JTextField();
        downloadFolderTf.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
        downloadFolderTf.setBounds(155, 90, 100, 25);

        add(downloadFolderTf);
    }

    private void addActionButtons() {
        JButton okBtn = new JButton(StringConst.OK);
        okBtn.setBounds(getRectangle().width - 225, getRectangle().height - 100, 100, 50);
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
        cancelBtn.setBounds(getRectangle().width - 115, getRectangle().height - 100, 100, 50);
        cancelBtn.setFocusable(false);
        cancelBtn.addActionListener(e -> closeFrame());
        add(cancelBtn);
    }

    private void addToggleBoxes() {
        JToggleButton button = new JToggleButton();
        button.setFocusable(false);
        button.setBounds(255, 90, 25, 25);
        button.addActionListener(e -> {
            FileChooser fileChooser = new FileChooser(this);
            fileChooser.open(JFileChooser.FILES_AND_DIRECTORIES);
        });

        add(button);
    }

    @Nullable
    private ScreenRecordOptions createScreenOptions() {
        if (!downloadFolderTf.getText().isEmpty()) {
            return new ScreenRecordOptions(
                    screenRecordWidth,
                    screenRecordHeight,
                    bitRate,
                    time,
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
