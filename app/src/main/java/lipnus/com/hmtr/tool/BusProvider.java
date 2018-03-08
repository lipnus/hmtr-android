package lipnus.com.hmtr.tool;

import com.squareup.otto.Bus;

/**
 * Created by LIPNUS on 2018-02-28.
 */

public final class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}