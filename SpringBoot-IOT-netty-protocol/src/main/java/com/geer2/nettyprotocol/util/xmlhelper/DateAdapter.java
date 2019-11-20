/**   
* 
*/
package com.geer2.nettyprotocol.util.xmlhelper;

import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @ClassName: DateAdapter 
 * @Description:
 * @author JiaweiWu
 */
public class DateAdapter extends XmlAdapter<String, Date> {
	private String pattern = "yyyy-MM-dd HH:mm:ss";

	@Override
	public Date unmarshal(String dateStr) throws Exception {
		//处理线程安全问题，每次重新创建一个新的SimpleDateFormat
		SimpleDateFormat fmt = new SimpleDateFormat(pattern);
		if (StringUtils.isNotEmpty(dateStr)) {
			Date parseDate = fmt.parse(dateStr);
			//使用完成后销毁对象
			fmt = null;
			return parseDate;
		} else {
			return null;
		}
	}

	@Override
	public String marshal(Date date) throws Exception {
		//处理线程安全问题，每次重新创建一个新的SimpleDateFormat
		SimpleDateFormat fmt = new SimpleDateFormat(pattern);
		if (date != null) {
			String formatDate = fmt.format(date);
			//使用完后销毁对象
			fmt = null;
			return formatDate;
		} else {
			return "";
		}
	}
}
