package com.example.racinglog.data.repository

import com.example.racinglog.data.model.Car
import com.example.racinglog.data.model.Track
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RacingCatalogRepository @Inject constructor() {

    fun getTracks(simulatorId: String): List<Track> {
        return when (simulatorId) {

            "lmu" -> listOf(
                Track("algarve", "Algarve International Circuit"),
                Track("bahrain", "Bahrain International Circuit"),
                Track("cota", "Circuit of the Americas"),
                Track("fuji", "Fuji Speedway"),
                Track("interlagos", "Interlagos"),
                Track("le_mans", "Le Mans"),
                Track("monza", "Monza"),
                Track("paul_ricard", "Paul Ricard"),
                Track("sebring", "Sebring"),
                Track("spa", "Spa-Francorchamps")
            )

            "ams2" -> listOf(
                Track("brasilia", "Brasília"),
                Track("campo_grande", "Campo Grande"),
                Track("curitiba", "Curitiba"),
                Track("galeao_airport", "Galeão Airport"),
                Track("guapore", "Guaporé"),
                Track("salvador", "Salvador"),
                Track("santa_cruz", "Santa Cruz do Sul"),
                Track("taruma", "Tarumã"),
                Track("velo_citta", "Velo Città"),
                Track("velopark", "Velopark")
            )

            "iracing" -> listOf(
                Track("barber", "Barber Motorsports Park"),
                Track("brands_hatch", "Brands Hatch"),
                Track("canadian_tire", "Canadian Tire Motorsport Park"),
                Track("homestead_miami", "Homestead-Miami Speedway"),
                Track("indianapolis", "Indianapolis Motor Speedway"),
                Track("kansas", "Kansas Speedway"),
                Track("okayama", "Okayama International Circuit"),
                Track("road_atlanta", "Road Atlanta"),
                Track("talladega", "Talladega Superspeedway"),
                Track("watkins_glen", "Watkins Glen International")
            )

            "acc" -> listOf(
                Track("barcelona", "Barcelona"),
                Track("hungaroring", "Hungaroring"),
                Track("misano", "Misano"),
                Track("monza", "Monza"),
                Track("nurburgring_gp", "Nürburgring GP"),
                Track("paul_ricard", "Paul Ricard"),
                Track("silverstone", "Silverstone"),
                Track("spa", "Spa-Francorchamps"),
                Track("zandvoort", "Zandvoort"),
                Track("zolder", "Zolder")
            )



            else -> emptyList()
        }
    }

    fun getCars(simulatorId: String): List<Car> {
        return when (simulatorId) {

            "lmu" -> listOf(
                Car("ferrari_499p", "Ferrari 499P", "Hypercar"),
                Car("porsche_963", "Porsche 963", "Hypercar"),
                Car(
                    "cadillac_vseriesr",
                    "Cadillac V-Series.R",
                    "Hypercar"
                ),
                Car("oreca_07", "Oreca 07", "LMP2"),
                Car(
                    "ferrari_296_gt3",
                    "Ferrari 296 GT3",
                    "LMGT3"
                ),
                Car(
                    "porsche_911_gt3r",
                    "Porsche 911 GT3 R",
                    "LMGT3"
                ),
                Car(
                    "mclaren_720s_gt3_evo",
                    "McLaren 720S GT3 Evo",
                    "LMGT3"
                )
            )

            "ams2" -> listOf(
                Car(
                    "formula_vee_gen2",
                    "Formula Vee Gen2",
                    "Formula Vee Gen2"
                ),
                Car(
                    "audi_r8_lms_gt3_evo2",
                    "Audi R8 LMS GT3 Evo II",
                    "GT3 Gen2"
                ),
                Car(
                    "lamborghini_huracan_gt3_evo2",
                    "Lamborghini Huracán GT3 EVO2",
                    "GT3 Gen2"
                ),
                Car(
                    "corvette_z06_gt3r",
                    "Chevrolet Corvette Z06 GT3.R",
                    "GT3 Gen2"
                ),
                Car(
                    "chevrolet_cruze_stock_2024",
                    "Chevrolet Cruze Stock Car 2024",
                    "Stock Car Pro Series 2024"
                ),
                Car(
                    "toyota_corolla_stock_2024",
                    "Toyota Corolla Stock Car 2024",
                    "Stock Car Pro Series 2024"
                )
            )

            "iracing" -> listOf(
                Car(
                    "mercedes_amg_gt3",
                    "Mercedes-AMG GT3",
                    "GT3"
                ),
                Car(
                    "acura_nsx_gt3_evo",
                    "Acura NSX GT3 Evo",
                    "GT3"
                ),
                Car(
                    "aston_martin_vantage_gt3_evo",
                    "Aston Martin Vantage GT3 Evo",
                    "GT3"
                ),
                Car(
                    "arca_chevrolet_ss",
                    "ARCA Chevrolet SS",
                    "ARCA Menards"
                ),
                Car(
                    "arca_ford_mustang",
                    "ARCA Ford Mustang",
                    "ARCA Menards"
                ),
                Car(
                    "arca_toyota_camry",
                    "ARCA Toyota Camry",
                    "ARCA Menards"
                ),
                Car(
                    "dallara_il15",
                    "Dallara IL-15",
                    "Indy NXT"
                )
            )


            "acc" -> listOf(
                Car(
                    "mclaren_720s_evo_gt3_2023",
                    "McLaren 720S Evo GT3 2023",
                    "GT3"
                ),
                Car(
                    "lamborghini_huracan_evo2_gt3_2023",
                    "Lamborghini Huracán EVO2 GT3 2023",
                    "GT3"
                ),
                Car(
                    "audi_r8_lms_evo2_gt3_2022",
                    "Audi R8 LMS Evo II GT3 2022",
                    "GT3"
                ),
                Car(
                    "alpine_a110_gt4_2018",
                    "Alpine A110 GT4 2018",
                    "GT4"
                ),
                Car(
                    "amr_v8_vantage_gt4_2018",
                    "AMR V8 Vantage GT4 2018",
                    "GT4"
                ),
                Car(
                    "audi_r8_lms_gt4_2018",
                    "Audi R8 LMS GT4 2018",
                    "GT4"
                ),
                Car(
                    "ktm_xbow_gt2",
                    "KTM X-Bow GT2",
                    "GT2"
                ),
                Car(
                    "maserati_mc20_gt2",
                    "Maserati MC20 GT2",
                    "GT2"
                ),
                Car(
                    "mercedes_amg_gt2",
                    "Mercedes-AMG GT2",
                    "GT2"
                )
            )
            else -> emptyList()
        }
    }
}

