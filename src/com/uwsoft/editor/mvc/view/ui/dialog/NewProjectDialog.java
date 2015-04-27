/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.mvc.view.ui.dialog;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.ui.widget.InputFileWidget;

import java.io.File;

public class NewProjectDialog extends VisDialog {
    public static final String CREATE_BTN_CLICKED = "com.uwsoft.editor.mvc.view.ui.dialog.NewProjectDialog" + ".CREATE_BTN_CLICKED";
    private static final String DEFAULT_ORIGIN_WITH = "1920";
    private static final String DEFAULT_ORIGIN_HEIGHT = "1200";

    private String defaultWorkspacePath;

    private final VisValidableTextField projectName;
    private final InputFileWidget workspacePathField;
    private final VisTextField originWithTextField;
    private final VisTextField originHeightTextField;

    NewProjectDialog() {
        super("Create New Project");

        setModal(true);
        addCloseButton();
        VisTable mainTable = new VisTable();

        VisLabel projectNameLavel = new VisLabel("Project Name:");
        mainTable.add(projectNameLavel).left().padRight(5);
        projectName = new VisValidableTextField(new SimpleFormValidator.EmptyInputValidator("Cannot be empty"));
        mainTable.add(projectName).width(120).left().fillX();
        mainTable.row().padBottom(15);

//        mainTable.debug();
        VisTable projectPathTable = new VisTable();
        projectPathTable.add(new VisLabel("Project Folder:")).right().padRight(5);
        workspacePathField = new InputFileWidget(FileChooser.Mode.OPEN, FileChooser.SelectionMode.DIRECTORIES, false);
        projectPathTable.add(workspacePathField);
        mainTable.add(projectPathTable).colspan(2).padBottom(15);
        //
        mainTable.row();
        //
        VisTextField.TextFieldFilter.DigitsOnlyFilter digitsOnlyFilter = new VisTextField.TextFieldFilter.DigitsOnlyFilter();
        VisTable projectResolutionTable = new VisTable();
        projectResolutionTable.add(new VisLabel("Original Resolution:")).right().padRight(5);
        originWithTextField = new VisTextField(DEFAULT_ORIGIN_WITH);
        originWithTextField.setTextFieldFilter(digitsOnlyFilter);
        projectResolutionTable.add(originWithTextField).width(50).padRight(3);
        projectResolutionTable.add(new VisLabel("X")).left().padRight(3);
        originHeightTextField = new VisTextField(DEFAULT_ORIGIN_HEIGHT);
        originHeightTextField.setTextFieldFilter(digitsOnlyFilter);
        projectResolutionTable.add(originHeightTextField).width(50).left();
        mainTable.add(projectResolutionTable).colspan(2).padBottom(15);
        //
        mainTable.row();
        //
        VisTextButton createBtn = new VisTextButton("Create New Project");

        mainTable.add(createBtn).colspan(2).right();
        //
        add(mainTable).pad(15);
        //
        createBtn.addListener(new BtnClickListener(CREATE_BTN_CLICKED));
    }

    @Override
    public VisDialog show(Stage stage, Action action) {
        originWithTextField.setText(DEFAULT_ORIGIN_WITH);
        originHeightTextField.setText(DEFAULT_ORIGIN_HEIGHT);
        workspacePathField.resetData();
        workspacePathField.setValue(new FileHandle(defaultWorkspacePath));
        return super.show(stage, action);
    }

    public String getOriginWidth() {
        return originWithTextField.getText();
    }

    public String getOriginHeight() {
        return originHeightTextField.getText();
    }

    private class BtnClickListener extends ClickListener {
        private final String command;

        public BtnClickListener(String command) {
            this.command = command;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            Overlap2DFacade facade = Overlap2DFacade.getInstance();
            facade.sendNotification(command, workspacePathField.getValue().path() + File.separator + projectName.getText());
        }
    }

    public String getDefaultWorkspacePath() {
        return defaultWorkspacePath;
    }

    public void setDefaultWorkspacePath(String defaultWorkspacePath) {
        this.defaultWorkspacePath = defaultWorkspacePath;
    }
}
