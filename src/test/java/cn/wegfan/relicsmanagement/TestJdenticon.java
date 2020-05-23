package cn.wegfan.relicsmanagement;

import cn.hutool.core.img.ImgUtil;
import com.github.atomfrede.jadenticon.Jadenticon;
import org.apache.batik.transcoder.TranscoderException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestJdenticon {

    public static void main(String[] args) throws IOException, TranscoderException {
        File file = Jadenticon.from("Jane Doe").withSize(300).png("test");
        ImgUtil.convert(file, new File("ee.jpg"));
        BufferedImage bg = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bg.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 300, 300);
        graphics.dispose();
        // ImageIO.write(bg, "jpg", new File("123123.jpg"));
        ImgUtil.pressImage(bg, ImageIO.read(file), 0, 0, 1.0f);
        ImgUtil.write(bg, new File("123123.jpg"));
        // FileUtil.writeFromStream(FileUtil.getInputStream(jdenticon), new File("aaa.png"));
    }

}
