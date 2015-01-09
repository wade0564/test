package cn.edu.ustc.wade.proxy;
public class ForumServiceImpl implements ForumService{
    public void removeTopic(int topicId){

        System.out.println("模拟删除记录"+topicId);
        try{
            Thread.currentThread().sleep(20);
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }

    public void removeForum(int forumId){
        System.out.println("模拟删除记录"+forumId);
        try{
            Thread.currentThread().sleep(20);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

}