package com.movmintdigitalfiat.icepic.utils;

import okhttp3.MediaType;

public class Constants {

    public static String icePicApiKey = "DcDTEcQHI5K/eNOfDyiGX5CLp1ZC2DoUyKe/AqivedE=";
    public static String icePicSecret = "OuJ4Q/8CibD3D6fwNB40wgCRPqmgeGhOO0clvAxTfDw=";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static String sandDollarApiKey = "NsbNt/9tMpDDwFyBGxNfg4FPALTmzpokZIrvS87PkRI=";
    public static String sandDollarSecret = "lig4Ye3cKWeyJiL1mnvmQTXhsomJCKyTGufbfCSjpTs=";

    public static String baseUrl = "https://api.testdollar.app";
    public static String baseUrlIce = "https://icepic-api.testdollar.app";
    public static String webViewUrl = "https://icepic.testdollar.app";
    public static String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
            "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAjdc1/do+ZTxkFCN1SKV1\n" +
            "pEigxJDyCsMGLXsP4bPqE1ZltQfd821/m4AMlMmxFl4JXe6PIO2vmIl1NN/4q6gf\n" +
            "Hsw3eDhe4YPdi79zEdc4xMGEv/km1i287jJVzyfjIOicBhysr/ald/vRh+CSHjZ7\n" +
            "UGYrCGnCwjeF+9+qAXyfOaAk0btxwKs4CVHPRtZKmUFXrlL+QE6shI17XJENV3P9\n" +
            "1whnoKislG51M6lk6BegvZKSWepzBmLJj6+zfG1Hhqpa47Yws+bIbB50ruKsGliX\n" +
            "Cf6d7efIzY1hk4R0s2BF2ArFbtwGHLv29lb3PMWqE1oF+fgfMQDRPEagfLZFylJq\n" +
            "93Ow7I4wc27C2VNi2nS3LdHsRlztHR3cGFAnToGOzqpjOJye6USJq4lcC7xHirr0\n" +
            "lmHK1KW9dcvhJxL3XvkbGF4lrGyYz0qOEDWmbK/1S4pNqYxRP3jkiGbDXfGsbIt9\n" +
            "MLtiGSVMNFXRtowL+U3dpN2rjkxTf8W2pi8fSwrlz4r9oAiKxoms7tJXa/bw6hx6\n" +
            "yrjh8Wd76agbK+rJQy7fWURmHBCUzZo8/kFPh+lOB6acGPfrb7wy5ayW7MFIz68o\n" +
            "MYRT5qv5q7wdcac8TKQtjWLG5lfuTaNAtU8oDe8pLXosmi1cNdhoGlFr8LJxCFDE\n" +
            "0VC7MNeGXEszC0LPRE4YxCcCAwEAAQ==\n" +
            "-----END PUBLIC KEY-----";

    public static String privateKey = ("MIIJQQIBADANBgkqhkiG9w0BAQEFAASCCSswggknAgEAAoICAQCN1zX92j5lPGQU\n" +
            "I3VIpXWkSKDEkPIKwwYtew/hs+oTVmW1B93zbX+bgAyUybEWXgld7o8g7a+YiXU0\n" +
            "3/irqB8ezDd4OF7hg92Lv3MR1zjEwYS/+SbWLbzuMlXPJ+Mg6JwGHKyv9qV3+9GH\n" +
            "4JIeNntQZisIacLCN4X736oBfJ85oCTRu3HAqzgJUc9G1kqZQVeuUv5ATqyEjXtc\n" +
            "kQ1Xc/3XCGegqKyUbnUzqWToF6C9kpJZ6nMGYsmPr7N8bUeGqlrjtjCz5shsHnSu\n" +
            "4qwaWJcJ/p3t58jNjWGThHSzYEXYCsVu3AYcu/b2Vvc8xaoTWgX5+B8xANE8RqB8\n" +
            "tkXKUmr3c7DsjjBzbsLZU2LadLct0exGXO0dHdwYUCdOgY7OqmM4nJ7pRImriVwL\n" +
            "vEeKuvSWYcrUpb11y+EnEvde+RsYXiWsbJjPSo4QNaZsr/VLik2pjFE/eOSIZsNd\n" +
            "8axsi30wu2IZJUw0VdG2jAv5Td2k3auOTFN/xbamLx9LCuXPiv2gCIrGiazu0ldr\n" +
            "9vDqHHrKuOHxZ3vpqBsr6slDLt9ZRGYcEJTNmjz+QU+H6U4HppwY9+tvvDLlrJbs\n" +
            "wUjPrygxhFPmq/mrvB1xpzxMpC2NYsbmV+5No0C1TygN7ykteiyaLVw12GgaUWvw\n" +
            "snEIUMTRULsw14ZcSzMLQs9EThjEJwIDAQABAoICAAlJkWKqngtJUp3Yjh8DnObB\n" +
            "tEUqq66PHO7XCZZg9KK8Txlbbbafiz6s7VkqTuSE1vgsXRLUfWFV65n0UF2gOuls\n" +
            "iPVGO6NojvxN8BFRYyL+/A8JF8UbMy1YyzJiDW0wmrnyIE++4EOXwx1MIKuTX0YU\n" +
            "aoNSgxe63i48pu6rlxsFPjC9chiIULllGFb7ERQxdk99ZhVJd+JcbgdrOARrg+N6\n" +
            "MwIO1iRuvYRBQzb8CEcuO1PH4vnRqudnPPyYt//WIVcBRvWAo8u9WSzz8QqIlagB\n" +
            "4M8DFKx6fwXs7/dCBoPu49HID/f+fSoEGvKci4mMC3gio86bNX6AFJVJGg8oj/he\n" +
            "KoWK1RUaj/SNb25vhSXjd53ln46Z3mSAQFYTPn9nO0zwJ2EK9cG+EzbyFKzbDdQT\n" +
            "K4BMM3SU3fMAQy61T75LhsfDDKRbRhtZWA+gYSDG56Oy34eOGe3FYeyHZ1UXN3D8\n" +
            "p1WwnhOpftI1RR6n7ctvNfMERTl4449r8QfbuPoMQn9kPSLYd3wD+QGmaalx/HHe\n" +
            "f894JnBo3GIXWtkzuL9+0TUDnbMkwNGriVWhnAIgQqmpbBhg1OyCdjnOQcwHL/oF\n" +
            "UAujUBOcf+xKwV+AYtrgoEUhxuhvUZpQE+iBYh47IQqSI8Ssi6jtKahG1ILKg7vl\n" +
            "6mnFt9DopnncHgKqi9MBAoIBAQDGuM9ZmezOwN0fIjx+TXa+3xP7Ru4lRQng3Sic\n" +
            "iwlhGHh35P2bHkvIVjW2M8FY8+PcXGi+xQYK6EchGDAy4pUY1oAqz9SpBxqDOEi4\n" +
            "nYVrmT6Mhuoft6s1lYUhbgTcIg5IBLTZrZe5zKZVLstq3bfrysZreODr1VKylPXK\n" +
            "yHbDNpCk7Un3x8LuNr+jAuyBXiK1uVcPZT44XcTJuM5ZHO9USa9oU84N0cNGTyNp\n" +
            "VeFFYiMsWAMyWCYd8QrjhPjFTNZktv1eoTCqgH9W55uuIK91vZ7/hPaC2BlxhG64\n" +
            "LK6Xb8J+jCZT0teE53stfSXS1qVi1Nd7gEHFg/CHAdMpPdTpAoIBAQC2uUbicFln\n" +
            "Agm5EtsdJKrqWOdUoPfPiNYCL87SCJOANL0rV1f0a5IdUI+ljRK0mUe/TM6FYunR\n" +
            "DEJdvpmN/zfDw//0NPg/7LKIOnGKrKgNBX3YFLxi0p9hft3LDMnlNNi1fkcvhevD\n" +
            "lE40U6+vehfittcdEbwr9E1oRxmANT9E1XJBppvs6XxXUu1E7+h8Bnj4Xi0+6zS8\n" +
            "rIXVE6si9WI7ebzuJzR5F50heuHXr8QYx5cme9Xos/Z6cKb96HOuV8CPEnNQp91l\n" +
            "lxOoSQHMe99vtkZEyzyZ/L9F+losndTD2tsex9O/K45xTYlYF2QsN3oyP4Tgmy9N\n" +
            "f/4ia5TnqmaPAoIBABqLQRck2l7boC2ycwYj7sKcJZ0g4lqUGDezEuOmJsCTdjP9\n" +
            "oGqB53mBGneV/jOVkFA0/8DLrHz0fdvL0VRas7O3ozMUw8E4M7Bnaj9Ouy2eMJ6w\n" +
            "vsEx1Wq46CmI+pk061dvWYDiESw+S9GBCKbjqteSJGLiqJiXzA4Sm6pUkJiOx09g\n" +
            "asrwIfeT1JE/qLu3CUfg0+I4kdERpWGUj+uTU9ycYxO6bgGmjUDs/TvqvsZ3z17y\n" +
            "+qpU3/KL6fpuHpSsKM8XKkzjlw6PT3MaSM8/inBdvck/x3W+y9c/61Iqlb+grAgf\n" +
            "TFdSCHu2N9vhpZW+GiVTbN0QSrjJYO2Hrt8JZVECggEAMMqwnu6ZMORebEkYsWOF\n" +
            "MCalPvfstEI5w0O42mTn3ig3C0e7eMzwq1tDwFU3VXpIqlB8JSoQfX3kitvGbSBl\n" +
            "Z6UCx9cNIyS1V6MjhoAsL63zuKtw0oKjhIk3+GxlzQqbg3tAzvPXzNXhPB/MnfLg\n" +
            "Re3E60I9GQ+XS+0QZifRyTOl2tn+cPi39ylxqiLV1ah0zP1434A3CNqYshkaba9Q\n" +
            "l8keJgnZ/SvNDY+eltWca3GAbwpKSGUlv/Qs40ga+8b66DlpCXSJrgL1gM5d6Jh7\n" +
            "RX1KrjwG5qeS+8CqK5fJpLLgKAv8AuRCmQa3UgqM+TGnM5u6g69spikzV0Wa8XGT\n" +
            "CQKCAQAr2s1pGaocKadmM73WPchG/1ZTwq2Gj2AER2oXXMkiDvmeCMvYjFVyydC5\n" +
            "KB+UXuMRSWHnmbdQGv47mUeIj/2cvFFCZrv+YV9PDKGjfFtGQ+4gvjLAZB0DwrjW\n" +
            "JUOpHwPj2Bc8g75PxzT/LYZw4sN4k7ak94IfRLKJ/NqlU9BFXvuyHQ9QOZY4WLiD\n" +
            "EJG7IAiK69mlQ6rlbjCtWzOcW4UXIGTvlTZtOworzGHgEc0djXHPGme+lmdnLFMz\n" +
            "FviUqdAQ+ylOeBAO6p5RkUSD29LONY9aZ+FW4UjsPA6KFXOPqUMUfwh7q06rC+5a\n" +
            "N2NdzfXxQ29qt2idqSnnuVFMf3Qc")
                .replaceAll("\\s", "")
                .replaceAll("\\n", "");
}
