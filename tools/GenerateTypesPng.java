import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class GenerateTypesPng {
    public static void main(String[] args) throws Exception {
        List<String> order = Arrays.asList(
                "normal","fire","water","electric","grass","ice","fighting","poison",
                "ground","flying","psychic","bug","rock","ghost","dragon","dark",
                "steel","fairy"
        );
        int cell = 16;
        int cols = 16;
        int width = 256, height = 256;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(new Font("SansSerif", Font.BOLD, 8));

        // Fill background transparent
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, width, height);
        g.setComposite(AlphaComposite.SrcOver);

        for (int i = 0; i < order.size(); i++) {
            int row = i / cols;
            int col = i % cols;
            int x = col * cell;
            int y = row * cell;
            Color c = colorFor(order.get(i));
            g.setColor(c);
            g.fillRoundRect(x + 1, y + 1, cell - 2, cell - 2, 4, 4);
            // Draw abbreviation
            String abbr = order.get(i).substring(0, Math.min(3, order.get(i).length())).toUpperCase();
            g.setColor(Color.WHITE);
            FontMetrics fm = g.getFontMetrics();
            int tx = x + (cell - fm.stringWidth(abbr)) / 2;
            int ty = y + (cell + fm.getAscent() - fm.getDescent()) / 2;
            g.drawString(abbr, tx, ty);
        }
        g.dispose();

        Path outDir = Path.of("/workspace/pixelmon-weakness-hud/src/main/resources/assets/pixelmon_weakness_hud/textures/gui");
        Files.createDirectories(outDir);
        File out = outDir.resolve("types.png").toFile();
        ImageIO.write(img, "png", out);
        System.out.println("Wrote: " + out.getAbsolutePath());
    }

    private static Color colorFor(String type) {
        switch (type) {
            case "fire": return new Color(0xF08030);
            case "water": return new Color(0x6890F0);
            case "electric": return new Color(0xF8D030);
            case "grass": return new Color(0x78C850);
            case "ice": return new Color(0x98D8D8);
            case "fighting": return new Color(0xC03028);
            case "poison": return new Color(0xA040A0);
            case "ground": return new Color(0xE0C068);
            case "flying": return new Color(0xA890F0);
            case "psychic": return new Color(0xF85888);
            case "bug": return new Color(0xA8B820);
            case "rock": return new Color(0xB8A038);
            case "ghost": return new Color(0x705898);
            case "dragon": return new Color(0x7038F8);
            case "dark": return new Color(0x705848);
            case "steel": return new Color(0xB8B8D0);
            case "fairy": return new Color(0xEE99AC);
            case "normal":
            default: return new Color(0xA8A878);
        }
    }
}