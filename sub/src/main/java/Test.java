

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

class DdfsDedup {
         
         static public class Range {
                   // Format: YYYY-MM-DD HH:mm:ss
                   Date startTime;
                   Date endTime;
         }
         
         static public class DdfsFragRange {
                   String fileName;
                   Range range;
                   
                   public DdfsFragRange() {range = new Range();}
                   
                   public DdfsFragRange(DdfsFragRange dfr) {
                            fileName = dfr.fileName;
                            range = new Range();
                            range.startTime = dfr.range.startTime;
                            range.endTime = dfr.range.endTime;
                   }
         }
         
         static private Comparator<Range> comparatorRange = new Comparator<Range>() {
                   public int compare(Range c1, Range c2) {
            return c1.startTime.compareTo(c2.startTime);
        }
         }; 
         
         static private Comparator<DdfsFragRange> comparatorDdfsFragRange = new Comparator<DdfsFragRange>() {
                   public int compare(DdfsFragRange c1, DdfsFragRange c2) {
            return c1.range.startTime.compareTo(c2.range.startTime);
        }
         }; 
         
         static private boolean laterOrEqual(Date d1, Date d2) {
                   return d1.after(d2) || d1.equals(d2);
         }

         /**
         * 
          * @param existedDdfs time range of existed ddfs files
         * @param newDdfsFrags time range of generated ddfs fragment files
         * @return list of ddfs fragment files and period to be generated 
          */
         static List<DdfsFragRange> dedup(List<Range> existedDdfs, List<DdfsFragRange> newDdfsFrags) {
                   
                   if (existedDdfs == null || existedDdfs.size() == 0) {
                            return newDdfsFrags;
                   }
                   
                   Collections.sort(existedDdfs, comparatorRange);
                   Collections.sort(newDdfsFrags, comparatorDdfsFragRange);
                   
                   int lastOld = existedDdfs.size()-1;
                   int lastNew = newDdfsFrags.size()-1;
                   
                   if (laterOrEqual(newDdfsFrags.get(0).range.startTime, existedDdfs.get(lastOld).endTime)) {
                            return newDdfsFrags;
                            
                   } else if (laterOrEqual(existedDdfs.get(0).startTime, newDdfsFrags.get(lastNew).range.endTime)) {
                            return newDdfsFrags;
                   
                   } 
                   
                   List<DdfsFragRange> result = new LinkedList<DdfsFragRange>(); 

                   if (laterOrEqual(existedDdfs.get(lastOld).endTime, newDdfsFrags.get(lastNew).range.endTime) && 
                                     laterOrEqual(newDdfsFrags.get(0).range.startTime, existedDdfs.get(0).startTime)) {                           
                            // in the middle
                            
                            for (int i=0; i<lastOld; ++i) {
                                     if ((existedDdfs.get(i).endTime.getTime() >= newDdfsFrags.get(0).range.startTime.getTime())
                                                        && (existedDdfs.get(i).endTime.getTime() <= newDdfsFrags.get(lastNew).range.endTime.getTime())) {
                                               
                                               Date smallDate = existedDdfs.get(i).endTime;
                                               Date bigDate = existedDdfs.get(i+1).startTime;
                                               
                                               for (int j=0; j<=lastNew; ++j) {
                                                        Range r = newDdfsFrags.get(j).range;
                                                        if (r.startTime.getTime() >= smallDate.getTime() && r.endTime.getTime() <= bigDate.getTime()) {
                                                                 result.add(newDdfsFrags.get(j));
                                                        } else if (r.startTime.getTime() < smallDate.getTime() && r.endTime.getTime() > smallDate.getTime()) {
                                                                 DdfsFragRange dfr = new DdfsFragRange(newDdfsFrags.get(j));
                                                                 dfr.range.startTime = smallDate;
                                                                 
                                                                 if (r.startTime.getTime() < bigDate.getTime() && r.endTime.getTime() > bigDate.getTime())
                                                                           dfr.range.endTime = bigDate;
                                                                 
                                                                 result.add(dfr);
                                                        } else if (r.startTime.getTime() < bigDate.getTime() && r.endTime.getTime() > bigDate.getTime()) {
                                                                 DdfsFragRange dfr = new DdfsFragRange(newDdfsFrags.get(j));
                                                                 dfr.range.endTime = bigDate;
                                                                 result.add(dfr);
                                                        }
                                               }
                                               
                                               break;
                                     }
                            }
                            
                                                        
                   } else if (laterOrEqual(newDdfsFrags.get(lastNew).range.endTime, existedDdfs.get(lastOld).endTime)) {
                            // a new ddfs
                            for (int i=lastNew; i>=0; i--) {
                                     if (laterOrEqual(newDdfsFrags.get(i).range.startTime, existedDdfs.get(lastOld).endTime)) {
                                               result.add(newDdfsFrags.get(i));
                                     } else {
                                               DdfsFragRange dfr = new DdfsFragRange(newDdfsFrags.get(i));
                                               dfr.range.startTime = existedDdfs.get(lastOld).endTime;
                                               result.add(dfr);
                                               break;
                                     }
                            }
                            
                   } else {
                            // a old ddfs
                            for (int i=0; i<=lastNew; i++) {
                                     if (laterOrEqual(existedDdfs.get(0).startTime, newDdfsFrags.get(i).range.endTime)) {
                                               result.add(newDdfsFrags.get(i));
                                     } else {
                                               DdfsFragRange dfr = new DdfsFragRange(newDdfsFrags.get(i));
                                               dfr.range.endTime = existedDdfs.get(0).startTime;
                                               result.add(dfr);
                                               break;
                                     }
                            }
                   }
                   
                   Collections.sort(result, comparatorDdfsFragRange);
                   return result;
         }
         
};


public class Test {
         
         void print(String s) {
                   System.out.println(s);
         }

         void print(int i) {
                   System.out.println(i);
         }

         void print(Date d) {
                   System.out.println(d.getTime());
         }
         
         void print(List<DdfsDedup.DdfsFragRange> r) {
                   for (int i=0; i<r.size(); ++i) {
                            print(r.get(i).fileName + " : " + r.get(i).range.startTime.getTime() + " - " + r.get(i).range.endTime.getTime());
                   }
         }
         
         List<DdfsDedup.Range> prepareExistedList() {
                   List<DdfsDedup.Range> existedDdfs = new ArrayList<DdfsDedup.Range>();
                   
                   DdfsDedup.Range r1 = new DdfsDedup.Range();
                   r1.startTime = new Date(10000);r1.endTime = new Date(20000);
                   DdfsDedup.Range r2 = new DdfsDedup.Range();
                   r2.startTime = new Date(30000);r2.endTime = new Date(40000);
                   DdfsDedup.Range r3 = new DdfsDedup.Range();
                   r3.startTime = new Date(40000);r3.endTime = new Date(50000);
                   
                   existedDdfs.add(r1);
                   existedDdfs.add(r2);
                   existedDdfs.add(r3);
                   
                   return existedDdfs;
         }
         
         List<DdfsDedup.DdfsFragRange> newDdfsFrags = new ArrayList<DdfsDedup.DdfsFragRange>(); 
         Test prepareDdfsFragRangeList() {
                   newDdfsFrags.clear();
                   return this;
         }
         
         Test prepareDdfsFragRangeList(String fileName, long start, long end) {
                   DdfsDedup.DdfsFragRange n1 = new DdfsDedup.DdfsFragRange(); 
                   n1.fileName=fileName;
                   n1.range.startTime = new Date(start);
                   n1.range.endTime = new Date(end);
                   newDdfsFrags.add(n1);
                   
                   return this;
         }
         
         List<DdfsDedup.DdfsFragRange> done() {
                   return newDdfsFrags;
         }
         
         void test1() {
                   List<DdfsDedup.Range> existedDdfs = prepareExistedList();
                   
                   List<DdfsDedup.DdfsFragRange> newDdfsFrags = 
                                     prepareDdfsFragRangeList()
                                     .prepareDdfsFragRangeList("f1", 15000, 18000)
                                     .prepareDdfsFragRangeList("f2", 18000, 32000)
                                     .done();
                   
                   List<DdfsDedup.DdfsFragRange> r = DdfsDedup.dedup(existedDdfs, newDdfsFrags);
                   
                   print(r);
         }
         
         void test2() {
                   List<DdfsDedup.Range> existedDdfs = prepareExistedList();
                   
                   List<DdfsDedup.DdfsFragRange> newDdfsFrags = 
                                     prepareDdfsFragRangeList()
                                     .prepareDdfsFragRangeList("f2", 60000, 70000)
                                     .prepareDdfsFragRangeList("f1", 40000, 60000)
                                     .done();
                   
                   List<DdfsDedup.DdfsFragRange> r = DdfsDedup.dedup(existedDdfs, newDdfsFrags);
                   
                   print(r);
         }

         void test3() {
                   List<DdfsDedup.Range> existedDdfs = prepareExistedList();
                   
                   List<DdfsDedup.DdfsFragRange> newDdfsFrags = 
                                     prepareDdfsFragRangeList()
                                     .prepareDdfsFragRangeList("f2", 5000, 20000)
                                     .prepareDdfsFragRangeList("f1", 1000, 3000)
                                     .prepareDdfsFragRangeList("f3", 20000, 30000)
                                     .done();
                   
                   List<DdfsDedup.DdfsFragRange> r = DdfsDedup.dedup(existedDdfs, newDdfsFrags);
                   
                   print(r);
         }

         public static void main(String args[]) {
                   Test t = new Test();
                   t.test1();
//                t.test2();
//                t.test3();
                   
         }
         
         
}

