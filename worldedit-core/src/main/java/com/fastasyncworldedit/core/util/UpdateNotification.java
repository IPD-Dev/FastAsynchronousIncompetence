package com.fastasyncworldedit.core.util;

import com.fastasyncworldedit.core.Fawe;
import com.fastasyncworldedit.core.FaweVersion;
import com.fastasyncworldedit.core.configuration.Caption;
import com.fastasyncworldedit.core.configuration.Settings;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.internal.util.LogManagerCompat;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.util.formatting.text.event.ClickEvent;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;

public class UpdateNotification {

    private static final Logger LOGGER = LogManagerCompat.getLogger();

    private static boolean hasUpdate;
    private static String faweVersion = "";

    /**
     * Check whether a new build with a higher build number than the current build is available.
     */
    public static void doUpdateCheck() {
        if (Settings.settings().ENABLED_COMPONENTS.UPDATE_NOTIFICATIONS) {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new URL("https://ci.plex.us.org/job/FastAsynchronousIncompetence/api/xml/").openStream());
                faweVersion = doc.getElementsByTagName("lastSuccessfulBuild").item(0).getFirstChild().getTextContent();
                FaweVersion faweVersion = Fawe.instance().getVersion();
                if (faweVersion.build == 0) {
                    LOGGER.warn("You are using a snapshot or a custom version of FAWE. This is not an official build distributed " +
                            "via https://www.spigotmc.org/resources/13932/");
                    return;
                }
                if (faweVersion.build < Integer.parseInt(UpdateNotification.faweVersion)) {
                    hasUpdate = true;
                    int versionDifference = Integer.parseInt(UpdateNotification.faweVersion) - faweVersion.build;
                    LOGGER.warn(
                            """
                                    An update for FastAsyncWorldEdit is available. You are {} build(s) out of date.
                                    You are running build {}, the latest version is build {}.
                                    Update at https://ci.plex.us.org/job/FastAsynchronousIncompetence""",
                            versionDifference,
                            faweVersion.build,
                            UpdateNotification.faweVersion
                    );
                }
            } catch (Exception e) {
                LOGGER.error("Unable to check for updates. Skipping.");
            }

        }
    }

    /**
     * Trigger an update notification based on captions. Useful to notify server administrators ingame.
     *
     * @param actor The player to notify.
     */
    public static void doUpdateNotification(Actor actor) {
        if (Settings.settings().ENABLED_COMPONENTS.UPDATE_NOTIFICATIONS) {
            if (actor.hasPermission("fawe.admin") && UpdateNotification.hasUpdate) {
                FaweVersion faweVersion = Fawe.instance().getVersion();
                int versionDifference = Integer.parseInt(UpdateNotification.faweVersion) - faweVersion.build;
                actor.print(Caption.of(
                        "fawe.info.update-available",
                        versionDifference,
                        faweVersion.build,
                        UpdateNotification.faweVersion,
                        TextComponent
                                .of("https://ci.plex.us.org/job/FastAsynchronousIncompetence/")
                                .clickEvent(ClickEvent.openUrl("https://ci.plex.us.org/job/FastAsynchronousIncompetence/"))
                ));
            }
        }
    }

}
