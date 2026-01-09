package com.wdiscute.starcatcher.io;

import com.wdiscute.starcatcher.storage.FishProperties;

public class Constellations
{

    public static class UrsaMinor
    {
        public static final FishProperties.Star POLARIS = FishProperties.Star.fromRaAndDec(
                "polaris", 3, 6, 50, 89f, 0xfff26969, "yildun");

        public static final FishProperties.Star YILDUN = FishProperties.Star.fromRaAndDec(
                "yildun", 17, 23, 35, 86f, 0xfff26969, "epsilon_umi");

        public static final FishProperties.Star EPSILON_UMI = FishProperties.Star.fromRaAndDec(
                "epsilon_umi", 16, 43, 15, 81f, 0xfff26969, "zeta_umi");

        public static final FishProperties.Star ZETA_UMI = FishProperties.Star.fromRaAndDec(
                "zeta_umi", 15, 43, 7, 77f, 0xfff26969, "kochab", "eta_umi");

        public static final FishProperties.Star KOCHAB = FishProperties.Star.fromRaAndDec(
                "kochab", 14, 50, 38, 74f, 0xfff26969, "pherkad");

        public static final FishProperties.Star ETA_UMI = FishProperties.Star.fromRaAndDec(
                "eta_umi", 16, 16, 42, 75f, 0xfff26969, "pherkad");

        public static final FishProperties.Star PHERKAD = FishProperties.Star.fromRaAndDec(
                "pherkad", 15, 20, 40, 71f, 0xfff26969, "");
    }

    public static class UrsaMajor
    {
        public static final FishProperties.Star ALKAID = FishProperties.Star.fromRaAndDec(
                "alkaid", 13, 48, 33, 49f, 0xff92f269, "mizar");

        public static final FishProperties.Star MIZAR = FishProperties.Star.fromRaAndDec(
                "mizar", 13, 24, 58, 54f, 0xff92f269, "alioth");

        public static final FishProperties.Star ALIOTH = FishProperties.Star.fromRaAndDec(
                "alioth", 12, 55, 10, 55f, 0xff92f269, "megrez");

        public static final FishProperties.Star MEGREZ = FishProperties.Star.fromRaAndDec(
                "megrez", 12, 16, 43, 56f, 0xff92f269, "phecda", "dubhe");

        public static final FishProperties.Star PHECDA = FishProperties.Star.fromRaAndDec(
                "phecda", 11, 55, 12, 53f, 0xff92f269, "merak");

        public static final FishProperties.Star MERAK = FishProperties.Star.fromRaAndDec(
                "merak", 11, 3, 25, 56f, 0xff92f269, "dubhe");

        public static final FishProperties.Star DUBHE = FishProperties.Star.fromRaAndDec(
                "dubhe", 11, 5, 21, 61f, 0xff92f269, "");
    }

    public static class Cassiopeia
    {
        public static final FishProperties.Star CAPH = FishProperties.Star.fromRaAndDec(
                "Caph", 0, 10, 34, 59f, 0xff69f2e0, "shedar");

        public static final FishProperties.Star SHEDAR = FishProperties.Star.fromRaAndDec(
                "shedar", 0, 41, 59, 56f, 0xff69f2e0,"navi");

        public static final FishProperties.Star NAVI = FishProperties.Star.fromRaAndDec(
                "navi", 0, 58, 17, 60f, 0xff69f2e0,"ruchbah");

        public static final FishProperties.Star RUCHBAH = FishProperties.Star.fromRaAndDec(
                "ruchbah", 1, 27, 32, 60f, 0xff69f2e0,"segin");

        public static final FishProperties.Star SEGIN = FishProperties.Star.fromRaAndDec(
                "segin", 1, 56, 18, 63f, 0xff69f2e0,"");
    }


    public static class TAURUS
    {
        public static final FishProperties.Star ELNATH = FishProperties.Star.fromRaAndDec(
                "elnath", 5, 27, 58, 28f, 0xff6d69f2, "tau_tari");

        public static final FishProperties.Star TAU_TARI = FishProperties.Star.fromRaAndDec(
                "tau_tari", 4, 43, 50, 23f,  0xff6d69f2, "ain");

        public static final FishProperties.Star AIN = FishProperties.Star.fromRaAndDec(
                "ain", 4, 30, 9, 19f, 0xff6d69f2,  "aldebaran", "gamma_tauri");

        public static final FishProperties.Star ALDEBARAN = FishProperties.Star.fromRaAndDec(
                "aldebaran", 4, 37, 26, 16f,  0xff6d69f2, "zeta_tauri", "gamma_tauri");

        public static final FishProperties.Star ZETA_TAURI = FishProperties.Star.fromRaAndDec(
                "zeta_tauri", 5, 39, 13, 21f,  0xff6d69f2, "");

        public static final FishProperties.Star GAMMA_TAURI = FishProperties.Star.fromRaAndDec(
                "gamma_tauri", 4, 21, 18, 15f,  0xff6d69f2, "lambda_tauri");

        public static final FishProperties.Star LAMBDA_TAURI = FishProperties.Star.fromRaAndDec(
                "lambda_tauri", 4, 2, 8, 12f,  0xff6d69f2, "omicron_tauri");

        public static final FishProperties.Star OMICRON_TAURI = FishProperties.Star.fromRaAndDec(
                "omicron_tauri", 3, 26, 14, 9f,  0xff6d69f2, "");
    }


}
