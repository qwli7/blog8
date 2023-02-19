package com.qwli7.blog.plugin.file;

import com.qwli7.blog.exception.BizException;
import com.qwli7.blog.exception.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author qwli7
 * @date 2023/2/17 11:28
 * 功能：blog8
 **/
@Component
public class FileService {

    private final Logger logger = LoggerFactory.getLogger(FileService.class);

    /**
     * 无效尺寸
     */
    private static final Resize INVALID_RESIZE = new Resize(-1, -1);
    /**
     * 默认尺寸
     */
    private static final Resize DEFAULT_SIZE = new Resize(0, 0);

    /**
     * 可处理的视频格式
     */
    private static final List<String> VIDEO_FORMAT_HANDLED = Arrays.asList(
            "mp4", "avi", "rmvb", "3gp"
    );

    /**
     * 可处理的图片格式
     */
    private static final List<String> IMAGE_FORMAT_HANDLED = Arrays.asList(
            "png", "jpeg", "webp", "gif", "jpg"
    );

    @Value("${blog.file.path}")
    private String blogFilePath;


    public List<FileInfo> fileUpload(FileUpload fileUpload) {

        List<MultipartFile> files = fileUpload.getFiles();
        if(CollectionUtils.isEmpty(files)) {
            throw new BizException(Message.AUTH_FAILED);
        }

        for(MultipartFile file: files) {
            String originalFilename = file.getOriginalFilename();
            try {
                Files.copy(file.getInputStream(), Paths.get(Paths.get(blogFilePath).toString(), originalFilename));
            } catch (IOException e) {
                logger.error("file upload failed. ", e);
            }
        }

        return new ArrayList<>();
    }

    public Optional<Resource> getFileInfo(String s) {
        Resize resize = resolveResize(s);
        if(DEFAULT_SIZE.getWidth() == resize.getWidth() && DEFAULT_SIZE.getHeight() == resize.getHeight()) {
            Path filePath = Paths.get(blogFilePath, s);
            if(Files.notExists(filePath)) {
                return Optional.empty();
            }
            LocalOriginalFileResource localOriginalFileResource = new LocalOriginalFileResource(filePath);
            return Optional.of(localOriginalFileResource);
        }
        int width = resize.getWidth();
        int height = resize.getHeight();
        if(resize.isScaleRatio()) {

            if(width != 0) {
                //执行视频图片缩放


                return Optional.empty();
            }

            if(height != 0) {

                return Optional.empty();
            }
        }

        if(resize.isForceScale()) {
            // 是否强制缩放
            // 宽高都存在，
            if(width != 0 && height != 0) {

            } else {
                if(width != 0) {

                }

                if(height != 0) {


                }

                return Optional.empty();
            }
        }

//        LocalOriginalFileResource localOriginalFileResource = new LocalOriginalFileResource(filePath);

        return Optional.empty();
    }

    private Resize resolveResize(String s) {
        String resizeStr = s.substring(s.lastIndexOf("/") + 1);
        if(StringUtils.isEmpty(resizeStr)) {
            //无缩放尺寸，说明访问路径以 / 结尾，这种不认为它是文件
            return INVALID_RESIZE;
        }
        //判断是否包含 .
        if(resizeStr.contains(".")) {
            // 表示应该返回原图
            //TODO 如果是视频或者图片类型的，就要做对应的压缩
            return new Resize(0, 0);
        }
        // 200 宽度缩放成 200 => 等比例缩放
        // x200 高度缩放成 200 => 等比例缩放
        // 200x200 宽度和高度均缩放成 200
        //200x200! 强制将宽度和高度缩放成 200
        // 200! 宽度强制缩放成 200
        // x200! 高度强制缩放成 200



        return new Resize(0, 0);
    }


    private boolean videoCanEdit(String extension) {
        return VIDEO_FORMAT_HANDLED.contains(extension);
    }

    private boolean imageCanEdit(String extension) {
        return IMAGE_FORMAT_HANDLED.contains(extension);
    }
}