package org.mcsg.survivalgames.logging;




public class QueueManagerOld1 {

 /*   private static QueueManagerOld instance = new QueueManagerOld();
    private DatabaseDumper dumper = new DatabaseDumper();
    private ArrayList<BlockData> queue = new ArrayList<BlockData>();
    private Plugin p;
    private  Logger log;
    private DatabaseManager dbman = DatabaseManager.getInstance();
    private boolean sqlmode = true;
    private QueueManagerOld1(){

    }

    public static QueueManagerOld getInstance(){
        return instance;
    }


    public void setup(Plugin p, boolean sqlmode) throws SQLException{
        this.sqlmode = sqlmode;
        this.p = p;

        if(sqlmode){
            PreparedStatement s = dbman.createStatement(" CREATE TABLE "+SettingsManager.getSqlPrefix() +"blocks(gameid int, world varchar(255),previd int,prevdata int,newid int, newdata int, x int, y int, z int, time long)");
            DatabaseMetaData dbm = dbman.getMysqlConnection().getMetaData();
            ResultSet tables = dbm.getTables(null, null, SettingsManager.getSqlPrefix() +"blocks", null);
            if (tables.next()) {

            }
            else {
                s.execute();
            }


            log = p.getLogger();
            log.info("Connected to database.");
        }

    }


    public void rollback(GameReset r,int id){
        GameManager.getInstance().getGame(id).setRBStatus("starting rollback");

        new preRollback(r, id).start();
    }

    public void add(BlockData data){
        queue.add(data);
        if(!dumper.isAlive() && sqlmode){
            dumper = new DatabaseDumper();
            dumper.start();
        }
    }

    class DatabaseDumper extends Thread{
        PreparedStatement s;
        public void run(){
            s =  dbman.createStatement("INSERT INTO "+SettingsManager.getSqlPrefix()+"blocks VALUES (?,?,?,?,?,?,?,?,?,?)");
            while(queue.size()>0){
                BlockData b = queue.remove(0);
                try{
                    s.setInt(1, GameManager.getInstance().getBlockGameId(new Location(Bukkit.getWorld(b.getWorld()),b.getX(), b.getY(), b.getZ())));
                    s.setString(2,b.getWorld());
                    s.setInt(3, b.getPrevid());
                    s.setByte(4, b.getPrevdata());
                    s.setInt(5, b.getNewid());
                    s.setByte(6, b.getNewdata());
                    s.setInt(7, b.getX());
                    s.setInt(8, b.getY());
                    s.setInt(9, b.getZ());
                    s.setLong(10, new Date().getTime());
                    s.execute();
                }catch(Exception e){
                    queue.add(b);
                    try {
                        dbman.getMysqlConnection().close();
                    } catch (SQLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    dbman.connect();

                }
            }
        }
    }


    class preRollback extends Thread{
        int id;
        Statement s;
        ResultSet result;
        GameReset r;
        Game game;
        int taskId = 0;
        private preRollback(GameReset r, int game){
            this.r = r;
            this.id = game;
            boolean done = false;
            this.game  = GameManager.getInstance().getGame(id);
        }
        public void run(){
            System.out.println(LoggingManager.i);
            game.setRBStatus("save queue");

            while(queue.size()>10 && sqlmode){
                game.setRBPercent(queue.size());
                try{sleep(10);}catch(Exception e){}
            }

            try{
                game.setRBStatus("querying");
                if(sqlmode){
                    String query = "SELECT * FROM "+SettingsManager.getSqlPrefix()+"blocks WHERE gameid="+id+" ORDER BY time DESC";
                    Statement s = dbman.createStatement();
                    game.setRBStatus("query result");

                    result = s.executeQuery(query);
                    game.setRBStatus("clearing entities");
                }

                try{
                    List<Entity> list = SettingsManager.getGameWorld(id).getEntities();
                    for (Iterator<Entity> entities = list.iterator();entities.hasNext();){
                        if (entities.hasNext()){
                            Entity entity = entities.next();
                            if (entity instanceof Item){
                                Item iteme = (Item) entity;
                                Location loce = entity.getLocation();
                                if(GameManager.getInstance().getBlockGameId(loce) == id){
                                    iteme.remove();
                                }
                            }
                        }
                    }
                }catch(Exception e){}
                game.setRBStatus("starting rollback");
                Rollback rb = new Rollback(result, r, id);
                int taskid =   Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(p,rb , 0, 1);
                rb.setTaskId(taskid);



            }catch(Exception e){e.printStackTrace();                dbman.connect();
            }




        }


    }




    class Rollback extends Thread{
        int id;
        Statement s;
        ResultSet result;
        GameReset r;
        Game game;
        int taskID = 0;
        private Rollback(ResultSet rs, GameReset r, int game){
            this.result = rs;
            this.r = r;
            this.id = game;
            boolean done = false;
            this.game  = GameManager.getInstance().getGame(id);
        }

        public void setTaskId(int t){
            taskID = t;
        }

        int rbblocks = 0;
        int total = 0;
        int run = 0;

        public void run(){
            if(sqlmode){
                try{
                    if(run == 0){
                        result.last();
                        total = result.getRow();
                        result.beforeFirst();
                        run++;
                    }

                    int i = 1;
                    boolean done = false;
                    try{

                        while(i != 100 && !done ){
                            game.setRBStatus("rollback "+1);

                            if(!result.next()){break;}

                            Location l = new Location(p.getServer().getWorld(result.getString(2)), result.getInt(7), result.getInt(8), result.getInt(9));
                            Block b = l.getBlock();
                            b.setTypeId(result.getInt(3));
                            b.setData(result.getByte(4));
                            b.getState().update();
                            i++;
                            rbblocks++;
                        }
                        game.setRBPercent(((0.0 + rbblocks) / (0.0+ total))*100);
                        if(i != 100){
                            game.setRBStatus("finish rollback");

                            done = true;
                            Bukkit.getScheduler().cancelTask(taskID);
                            game.setRBStatus("rollback stoppped");

                            r.rollbackFinishedCallback();
                            game.setRBStatus("clearing table");

                            Statement s1 = dbman.createStatement(); 
                            s1.execute("DELETE FROM "+SettingsManager.getSqlPrefix()+"blocks WHERE gameid="+id);
                            System.out.println("Arena "+id+" reset. Rolled back "+rbblocks+" blocks");
                            game.setRBStatus("");

                            result.close();
                            result = null;
                        }
                    }catch(Exception e){e.printStackTrace();}
                }catch(Exception e){e.printStackTrace();}
            }
            else{
                int a = queue.size()-1;
                int rbblocks = 0;
                while(a>=0){
                    BlockData result = queue.get(a);
                    if(result.getGameId() == game.getID()){
                        queue.remove(a);
                        Location l = new Location(Bukkit.getWorld(result.getWorld()), result.getX(), result.getY(), result.getZ());
                        Block b = l.getBlock();
                        b.setTypeId(result.getPrevid());
                        b.setData(result.getPrevdata());
                        b.getState().update();
                        rbblocks++;
                       
                    }
                    a--;
                }
                System.out.println("Arena "+id+" reset. Rolled back "+rbblocks+" blocks. Save contains "+queue.size()+" blocks");

                Bukkit.getScheduler().cancelTask(taskID);
                r.rollbackFinishedCallback();
                
            }
        }


    }*/
}


