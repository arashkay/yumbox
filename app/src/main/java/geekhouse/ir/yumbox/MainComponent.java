package geekhouse.ir.yumbox;

import javax.inject.Singleton;

import dagger.Component;
import geekhouse.ir.yumbox.data.DataModule;
import geekhouse.ir.yumbox.data.api.ApiModule;
import geekhouse.ir.yumbox.ui.MainActivity;
import geekhouse.ir.yumbox.ui.OrderActivity;
import geekhouse.ir.yumbox.ui.OrderHistory;
import geekhouse.ir.yumbox.ui.fragments.IntroFragment;
import geekhouse.ir.yumbox.ui.fragments.MainActivityFragment;

@Singleton
@Component(modules = {
        YumboxModule.class,
        DataModule.class,
        ApiModule.class
})
public interface MainComponent {

    void inject(MainActivity mainActivity);
    void inject(IntroFragment introFragment);
    void inject(MainActivityFragment mainActivityFragment);
    void inject(OrderActivity orderActivity);
    void inject(OrderHistory orderHistory);
}