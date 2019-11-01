package gs;

import org.neo4j.graphdb.Node

public class NodeLife 
{
    Node node;
    int life;
    String [] words;

    public NodeLife(Node node, int life, String [] words)
    {
        this.node = node;
        this.life = life;
        this.words = words.split(" ");
    }
    
}
