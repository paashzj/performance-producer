package com.github.shoothzj.pf.producer.http;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hezhangjian
 */
@Slf4j
public class HttpUtil {

    public static String getHttpData() {
        return "{\n"
                + "      \"name\": \"Avengers: Endgame\",\n"
                + "      \"years\": 2019,\n"
                + "      \"like\": true,\n"
                + "      \"heroes\": [\n"
                + "        {\n"
                + "          \"name\": \"Tony Stark\",\n"
                + "          \"nickName\": \"Iron Man\",\n"
                + "          \"actor\": \"Robert Downey\",\n"
                + "          \"age\": 48\n"
                + "        }, {\n"
                + "          \"name\": \"Steve Rogers\",\n"
                + "          \"nickName\": \"Captain America\",\n"
                + "          \"actor\": \"Chris Evans\",\n"
                + "          \"age\": 100\n"
                + "        }],\n"
                + "      \"marvelInfo\": {\n"
                + "        \"sequence\": 22,\n"
                + "        \"hasIronMan\": true\n"
                + "      },\n"
                + "      \"null\": null\n"
                + "    }";
    }

}
