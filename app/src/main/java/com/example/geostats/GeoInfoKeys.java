package com.example.geostats;

import java.util.HashMap;

public class GeoInfoKeys {

    private static GeoInfoKeys sGeoInfoKeys;

    private HashMap<String, String> mGeoInfoKeys = new HashMap<>();

    public static GeoInfoKeys getInstance() {
        if (sGeoInfoKeys == null) {
            sGeoInfoKeys = new GeoInfoKeys();
        }

        return sGeoInfoKeys;
    }

    private GeoInfoKeys() {
        mGeoInfoKeys.put("TOTPOP", "Total Population");
        mGeoInfoKeys.put("TOTHH", "Total Households");
        mGeoInfoKeys.put("AVGHHSZ", "Average Household Size");
        mGeoInfoKeys.put("TOTMALES", "Male Population");
        mGeoInfoKeys.put("TOTFEMALES", "Female Population");
        mGeoInfoKeys.put("TOTPOP00", "2000 Total Population");
        mGeoInfoKeys.put("TOTPOP10", "2010 Total Population");
        mGeoInfoKeys.put("TOTPOP_CY", "2019 Total Population");
        mGeoInfoKeys.put("TOTPOP_FY", "2024 Total Population");
        mGeoInfoKeys.put("GQPOP_CY", "2019 Population in Group Quarters");
        mGeoInfoKeys.put("DIVINDX_CY", "2019 Diversity Index");
        mGeoInfoKeys.put("TOTHH00", "2000 Total Households");
        mGeoInfoKeys.put("TOTHH10", "2010 Total Households");
        mGeoInfoKeys.put("TOTHH_CY", "2019 Total Households");
        mGeoInfoKeys.put("TOTHH_FY", "2024 Total Households");
        mGeoInfoKeys.put("AVGHHSZ_CY", "2019 Average Household Size");
        mGeoInfoKeys.put("MEDHINC_CY", "2019 Median Household Income");
        mGeoInfoKeys.put("MEDHINC_FY", "2024 Median Household Income");
        mGeoInfoKeys.put("AVGHINC_CY", "2019 Average Household Income");
        mGeoInfoKeys.put("AVGHINC_FY", "2024 Average Household Income");
        mGeoInfoKeys.put("PCI_CY", "2019 Per Capita Income");
        mGeoInfoKeys.put("PCI_FY", "2024 Per Capita Income");
        mGeoInfoKeys.put("TOTHU00", "2000 Total Housing Units");
        mGeoInfoKeys.put("TOTHU10", "2010 Total Housing Units");
        mGeoInfoKeys.put("TOTHU_CY", "2019 Total Housing Units");
        mGeoInfoKeys.put("TOTHU_FY", "2024 Total Housing Units");
        mGeoInfoKeys.put("OWNER_CY", "2019 Owner Occupied HUs");
        mGeoInfoKeys.put("OWNER_FY", "2024 Owner Occupied HUs");
        mGeoInfoKeys.put("RENTER_CY", "2019 Renter Occupied HUs");
        mGeoInfoKeys.put("RENTER_FY", "2024 Renter Occupied HUs");
        mGeoInfoKeys.put("VACANT_CY", "2019 Vacant Housing Units");
        mGeoInfoKeys.put("VACANT_FY", "2024 Vacant Housing Units");
        mGeoInfoKeys.put("MEDVAL_CY", "2019 Median Home Value");
        mGeoInfoKeys.put("MEDVAL_FY", "2024 Median Home Value");
        mGeoInfoKeys.put("AVGVAL_CY", "2019 Average Home Value");
        mGeoInfoKeys.put("AVGVAL_FY", "2024 Average Home Value");
        mGeoInfoKeys.put("POPGRW10CY", "2010-2019 Growth Rate: Population");
        mGeoInfoKeys.put("HHGRW10CY", "2010-2019 Growth Rate: Households");
        mGeoInfoKeys.put("FAMGRW10CY", "2010-2019 Growth Rate: Families");
        mGeoInfoKeys.put("POPGRWCYFY", "2019-2023 Growth/Yr: Population");
        mGeoInfoKeys.put("HHGRWCYFY", "2019-2023 Growth/Yr: Households");
        mGeoInfoKeys.put("FAMGRWCYFY", "2019-2023 Growth/Yr: Families");
        mGeoInfoKeys.put("MHIGRWCYFY", "2019-2023 Growth/Yr: Median HH Inc");
        mGeoInfoKeys.put("PCIGRWCYFY", "2019-2023 Growth/Yr: Per Capita Income");
        mGeoInfoKeys.put("DPOP_CY", "2019 Total Daytime Population");
        mGeoInfoKeys.put("DPOPWRK_CY", "2019 Daytime Pop: Workers");
        mGeoInfoKeys.put("DPOPRES_CY", "2019 Daytime Pop: Residents");
    }

    public HashMap<String, String> getGeoInfoKeys() {
        return mGeoInfoKeys;
    }
}
