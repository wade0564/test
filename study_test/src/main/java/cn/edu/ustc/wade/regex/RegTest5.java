package cn.edu.ustc.wade.regex;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegTest5 {

	public static void main(String[] args) throws IOException {

		String name = "c:/tmp/unzip/ddr/var/log/debug/messages.engineering.1.gz";

		Pattern pattern = Pattern
				.compile("messages.engineering(|\\.\\d+)(|\\.gz)$");

		Matcher matcher = pattern.matcher(name);

		System.out.println(matcher.find());

	}
}