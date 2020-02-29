package com.alipay.rdf.file.resource;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.alipay.rdf.file.interfaces.FileFactory;
import com.alipay.rdf.file.interfaces.FileStorage;
import com.alipay.rdf.file.loader.TemplateLoader;
import com.alipay.rdf.file.meta.FileColumnMeta;
import com.alipay.rdf.file.meta.FileMeta;
import com.alipay.rdf.file.model.FileDefaultConfig;
import com.alipay.rdf.file.model.StorageConfig;
import com.alipay.rdf.file.util.OssTestUtil;

/**
 * 模板加载测试
 * 
 * @author hongwei.quhw
 * @version $Id: TemplateLoaderTest.java, v 0.1 2016-12-22 下午2:01:39 hongwei.quhw Exp $
 */
public class OssTemplateLoaderTest {
    private static final String        OSS_PATH      = "rdf/rdf-file/resources/";
    private static final StorageConfig storageConfig = OssTestUtil.geStorageConfig();

    private FileStorage                fileStorage   = FileFactory.createStorage(storageConfig);

    @Before
    public void setUp() {
        FileDefaultConfig defaultConfig = new FileDefaultConfig();
        defaultConfig.addDefaultFleParam("oss", storageConfig);
        defaultConfig.setRdfTemplatePath("oss:");
    }

    /**
     * 路径指定协议
     */
    @Test
    public void testOssLoader() {
        String ossPath = OSS_PATH + "oss.json";
        fileStorage.upload(File.class.getResource("/resource/de-utf-8.json").getPath(), ossPath,
            true);

        FileMeta fileMeta = TemplateLoader.load(ossPath, "UTF-8");
        validate(fileMeta);

    }

    /**
     * 路径指定协议
     */
    @Test
    public void testOssLoader2() {
        String ossPath = OSS_PATH + "oss.json";
        fileStorage.upload(File.class.getResource("/resource/de-utf-8.json").getPath(), ossPath,
            true);

        FileMeta fileMeta = TemplateLoader.load("oss:" + ossPath, "UTF-8");
        validate(fileMeta);

    }

    private void validate(FileMeta fileMeta) {
        List<FileColumnMeta> headMetas = fileMeta.getHeadColumns();
        Assert.assertEquals(2, headMetas.size());

        Assert.assertEquals("recordId", headMetas.get(0).getName());
        Assert.assertEquals("recordId", headMetas.get(0).getDesc());

        Assert.assertEquals("acctRef", headMetas.get(1).getName());
        Assert.assertEquals("测试", headMetas.get(1).getDesc());

        List<FileColumnMeta> bodyMetas = fileMeta.getBodyColumns();
        Assert.assertEquals(7, bodyMetas.size());

        Assert.assertEquals("mfundAccountNo", bodyMetas.get(2).getName());
        Assert.assertEquals("货币基金份额账户", bodyMetas.get(2).getDesc());
        Assert.assertEquals("BigDecimal", bodyMetas.get(2).getType().getName());

        Assert.assertEquals("date", bodyMetas.get(3).getName());
        Assert.assertEquals("日期类型", bodyMetas.get(3).getDesc());
        Assert.assertEquals("Date", bodyMetas.get(3).getType().getName());
        Assert.assertEquals("yyyy-MM-dd HH:mm:ss", bodyMetas.get(3).getType().getExtra());

        Assert.assertEquals("count", bodyMetas.get(4).getName());
        Assert.assertEquals("金额", bodyMetas.get(4).getDesc());
        Assert.assertEquals("BigDecimal", bodyMetas.get(4).getType().getName());
        Assert.assertNull(bodyMetas.get(4).getType().getExtra());
        Assert.assertEquals(10, bodyMetas.get(4).getRange().getFirstAttr());
        Assert.assertEquals(2, bodyMetas.get(4).getRange().getSecondAttr());

        Assert.assertEquals("name", bodyMetas.get(5).getName());
        Assert.assertEquals("姓名", bodyMetas.get(5).getDesc());
        Assert.assertEquals("String", bodyMetas.get(5).getType().getName());
        Assert.assertNull(bodyMetas.get(5).getType().getExtra());
        Assert.assertTrue(bodyMetas.get(5).isRequired());
        Assert.assertEquals("jack", bodyMetas.get(5).getDefaultValue());

        Assert.assertEquals("test", bodyMetas.get(6).getName());
        Assert.assertEquals("所有", bodyMetas.get(6).getDesc());
        Assert.assertEquals("String", bodyMetas.get(6).getType().getName());
        Assert.assertNull(bodyMetas.get(6).getType().getExtra());
        Assert.assertFalse(bodyMetas.get(6).isRequired());
        Assert.assertEquals("jack", bodyMetas.get(6).getDefaultValue());
        Assert.assertEquals(20, bodyMetas.get(6).getRange().getFirstAttr());
        Assert.assertEquals(23, bodyMetas.get(6).getRange().getSecondAttr());

        Assert.assertEquals("totalCount", fileMeta.getTotalCountKey());
    }

    @After
    public void after() {
        fileStorage.delete(OSS_PATH);
        FileDefaultConfig.RDF_PROCESSOR_PATH = "classpath*:META-INF/rdf-file/auto-processor/";
        FileDefaultConfig.RDF_PROTOCOL_PATH = "classpath*:META-INF/rdf-file/protocol/";
        FileDefaultConfig.RDF_FORMAT_PATH = "classpath*:META-INF/rdf-file/format/";
        FileDefaultConfig.RDF_TEMPLATE_PATH = "classpath:";
    }
}
