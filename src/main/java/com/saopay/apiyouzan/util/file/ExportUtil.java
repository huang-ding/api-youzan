package com.saopay.apiyouzan.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * 文件导出工具类
 *
 */
public class ExportUtil {
	private ExportUtil() {

	}

	public static void packageHttpFileToZip(String exportFileName, List<String> urls, HttpServletRequest request, HttpServletResponse response) {
		if(CollectionUtils.isNotEmpty(urls)) {
			ZipArchiveOutputStream out = null;
			OutputStream outputStream = null;
			try {
				outputStream = response.getOutputStream();
				out = new ZipArchiveOutputStream(outputStream);
				setExportInfo(exportFileName, request, response);
				int index = 1;
				for(String url : urls) {
					URL remoteUrl = new URL(url);
					File localFile = new File(request.getSession().getServletContext().getRealPath("/"), remoteUrl.getPath());
					FileUtils.copyURLToFile(remoteUrl, localFile);
					out.putArchiveEntry(new ZipArchiveEntry(localFile, index + "_" + localFile.getName()));
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(localFile);
						IOUtils.copy(fis, out);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						IOUtils.closeQuietly(fis);
					}
					out.closeArchiveEntry();
					index++;
					FileUtils.deleteDirectory(localFile.getParentFile());
				}
				out.flush();
				out.finish();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(out);
				IOUtils.closeQuietly(outputStream);
			}
		}
	}
	
	/**
	 * 
	 * 描述： 设置文件导出 <br/>
	 * 作者： ZhangHeng
	 * 
	 * @param exportFileName
	 * @param request
	 * @param response void <br/>
	 * @throws UnsupportedEncodingException
	 */
	private static void setExportInfo(String exportFileName, HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		response.setCharacterEncoding("UTF-8");
		response.reset();
		response.setContentType("x-octet-stream");
		String fileName = URLEncoder.encode(exportFileName, "UTF-8");
		String UserAgent = request.getHeader("USER-AGENT").toLowerCase();
		if (UserAgent.indexOf("firefox") >= 0) {
			fileName = new String((exportFileName).getBytes("UTF-8"), "iso-8859-1");
		}
		response.setHeader("Content-disposition", "attachment;filename=" + fileName);
	}
}
