package client.modules;

import client.windows.lists.cells.CardService;
import client.windows.lists.list.ListCtrl;
import client.windows.tags.TagOverviewCtrl;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

public class ListModules implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(ListCtrl.class).in(Scopes.SINGLETON);
        binder.bind(CardService.class).in(Scopes.SINGLETON);
        binder.bind(TagOverviewCtrl.class).in(Scopes.SINGLETON);
    }
}
