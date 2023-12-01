/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.fragment;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.SystemProp;
import pl.polsl.screensharing.lib.file.Alignment;
import pl.polsl.screensharing.lib.file.FileUtils;
import pl.polsl.screensharing.lib.gui.component.JAppLink;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.StringJoiner;

@Slf4j
public class JAppAboutPanel extends JPanel {
    private final AppType appType;

    private final JPanel imageContent;
    private final JPanel rightContent;
    private final JPanel bottomContent;
    private final JPanel legalContent;

    private final JLabel iconLabel;
    private final JLabel titleLabel;
    private final JLabel aboutLabel;
    private final JAppLink repoLinkLabel;
    private final JAppLink iconsLinkLabel;
    private final JLabel jvmProperties;

    public JAppAboutPanel(AppType appType) {
        this.appType = appType;

        imageContent = new JPanel();
        rightContent = new JPanel();
        bottomContent = new JPanel();
        legalContent = new JPanel();

        iconLabel = new JLabel(loadImageIcon());
        titleLabel = new JLabel(appType.getRootWindowTitle());
        aboutLabel = new JLabel(loadDescription());
        repoLinkLabel = new JAppLink("https://github.com/Milosz08/screen-sharing-system", "Github Repo");
        iconsLinkLabel = new JAppLink("https://www.microsoft.com/en-us/download/details.aspx?id=35825", "Icons by MS Visual Studio Icons");
        jvmProperties = new JLabel(generateJvmProperties());

        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 16));
        jvmProperties.setForeground(Color.GRAY);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        imageContent.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        imageContent.add(iconLabel);

        legalContent.setLayout(new BoxLayout(legalContent, BoxLayout.Y_AXIS));
        legalContent.add(repoLinkLabel);
        legalContent.add(iconsLinkLabel);

        bottomContent.setLayout(new BoxLayout(bottomContent, BoxLayout.X_AXIS));
        bottomContent.add(jvmProperties);
        bottomContent.add(legalContent);

        rightContent.setLayout(new GridLayout(3, 1, 0, 0));
        rightContent.add(titleLabel);
        rightContent.add(aboutLabel);
        rightContent.add(bottomContent);

        add(imageContent);
        add(rightContent);
    }

    private String generateJvmProperties() {
        return new StringJoiner(StringUtils.EMPTY)
            .add("<html>")
            .add(String.format("JVM: %s<br>", SystemProp.JVM_VERSION.getProp()))
            .add(String.format("OS: %s", SystemProp.OS_VERSION.getProp()))
            .add("</html>")
            .toString();
    }

    private ImageIcon loadImageIcon() {
        final String iconName = appType.getIconName();
        try {
            final URL url = FileUtils.getAssetFileFromResources(getClass(), iconName + ".png")
                .orElseThrow(RuntimeException::new);
            final Image scaledImage = ImageIO.read(url).getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            log.info("Successfully load {} asset file from asset resources directory.", iconName);
            return new ImageIcon(scaledImage);
        } catch (Exception ex) {
            log.error("Unable to load {} icon from assets resource directory.", iconName);
            System.exit(-1);
        }
        return null;
    }

    private String loadDescription() {
        try {
            return FileUtils.loadAndWrapAsHtmlContent("description.txt", getClass(), Alignment.LEFT);
        } catch (Exception ex) {
            log.error("Unable to load description from assets resource directory.");
            System.exit(-1);
        }
        return StringUtils.EMPTY;
    }
}
