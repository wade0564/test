package cn.edu.ustc.wade.mail;

import java.util.Calendar;
import java.util.Date;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SentDateTerm;


public class SubTermStrategy  {

	public SearchTerm generateSearchTerm(Flags supportedFlags, Folder folder) {
		
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, calendar.get(Calendar.DAY_OF_WEEK - (Calendar.DAY_OF_WEEK - 1)) - 1);
		Date mondayDate = calendar.getTime();
		SearchTerm seen =new FlagTerm(new Flags(Flags.Flag.SEEN),true);
		SearchTerm comparisonTermGe = new SentDateTerm(ComparisonTerm.GE, mondayDate);
		SearchTerm comparisonTermLe = new SentDateTerm(ComparisonTerm.LE, new Date());
		SearchTerm comparisonAndTerm = new AndTerm(comparisonTermGe, comparisonTermLe);
		SearchTerm s1=new FlagTerm(new Flags(Flags.Flag.DELETED), true);
		SearchTerm s3=new FromStringTerm("Zhang, Wade");
		SearchTerm s=new AndTerm( comparisonAndTerm,new FromStringTerm("Zhang, Wade"));
		s=new AndTerm(seen,s);
		
		return s;
	}

}
