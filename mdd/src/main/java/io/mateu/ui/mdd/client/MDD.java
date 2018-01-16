package io.mateu.ui.mdd.client;

import io.mateu.ui.core.client.app.Callback;
import io.mateu.ui.core.client.app.MateuUI;
import io.mateu.ui.core.shared.Data;
import io.mateu.ui.mdd.shared.ERPService;

public class MDD {

    public static void openCRUD(Class entityClass) {
        openCRUD(entityClass, null);
    }

    public static void openCRUD(Class entityClass, String queryFilters) {
        ((ERPServiceAsync) MateuUI.create(ERPService.class)).getMetaData(MateuUI.getApp().getUserData(), entityClass.getName(), entityClass.getName(), queryFilters, new MDDCallback());
    }

    public static void openEditor(Class entityClass, Object id) {
        ((ERPServiceAsync) MateuUI.create(ERPService.class)).getMetaData(MateuUI.getApp().getUserData(), entityClass.getName(), entityClass.getName(), null, new Callback<Data>() {
            @Override
            public void onSuccess(Data result) {
                MateuUI.openView(new MDDJPACRUDView(result).getNewEditorView().setInitialId(id));
            }
        });
    }

    public static void openView(Class entityClass, Data data) {
        openView(entityClass, data, null);
    }

    public static void openView(Class entityClass, Data data, String queryFilters) {
        ((ERPServiceAsync) MateuUI.create(ERPService.class)).getMetaData(MateuUI.getApp().getUserData(), entityClass.getName(), entityClass.getName(), queryFilters, new MDDCallback() {
            @Override
            public void onSuccess(Data result) {
                MateuUI.openView(new MDDJPACRUDView(result) {
                    @Override
                    public Data initializeData() {
                        return data;
                    }
                });
            }
        });
    }

}
