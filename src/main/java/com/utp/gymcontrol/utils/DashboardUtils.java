package com.utp.gymcontrol.utils;

import com.google.common.collect.Lists;
import java.util.List;

public class DashboardUtils {

    public static List<String> obtenerModulos() {

        return Lists.newArrayList(
                "Socios",
                "Membresias",
                "Pagos",
                "Reportes"
        );
    }
}
