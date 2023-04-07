/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.modules;

import client.MainCtrl;
import client.utils.DataFormatManager;
import client.utils.HelperMethods;
import client.windows.adminview.boardCell.BoardCellCtrl;
import client.windows.cards.add.AddCardCtrl;
import client.windows.cards.view.ViewCardCtrl;
import client.windows.customize.CustomizeCtrl;
import client.windows.customize.cards.add.AddCardPresetCtrl;
import client.windows.customize.cards.add.AddCardPresetService;
import client.windows.customize.cards.edit.EditCardPresetCtrl;
import client.windows.customize.cards.edit.EditCardPresetService;
import client.windows.lists.cells.CardService;
import client.windows.lists.delete.DeleteListCtrl;
import client.windows.lists.list.ListCtrl;
import client.windows.login.admin.AdminLoginCtrl;
import client.windows.login.user.UserLoginCtrl;
import client.windows.tags.add.AddTagCtrl;
import client.windows.tags.view.TagListCtrl;
import client.windows.tags.view.TagOverviewCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

public class MainModules implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(UserLoginCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AdminLoginCtrl.class).in(Scopes.SINGLETON);
        binder.bind(WorkspaceCtrl.class).in(Scopes.SINGLETON);
        binder.bind(HelperMethods.class).in(Scopes.SINGLETON);
        binder.bind(AddCardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ViewCardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(TagListCtrl.class).in(Scopes.SINGLETON);
        binder.bind(DeleteListCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddCardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ListCtrl.class).in(Scopes.SINGLETON);
        binder.bind(CardService.class).in(Scopes.SINGLETON);
        binder.bind(BoardCellCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddTagCtrl.class).in(Scopes.SINGLETON);
        binder.bind(TagOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(CustomizeCtrl.class).in(Scopes.SINGLETON);
        binder.bind(DataFormatManager.class).in(Scopes.SINGLETON);
        binder.bind(AddCardPresetCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddCardPresetService.class).in(Scopes.SINGLETON);
        binder.bind(EditCardPresetCtrl.class).in(Scopes.SINGLETON);
        binder.bind(EditCardPresetService.class).in(Scopes.SINGLETON);
    }
}
