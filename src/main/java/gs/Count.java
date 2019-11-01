package gs;

import gs.NodeLife;

import java.util.ArrayList;
import java.util.Collections;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

public class Count
{

    @UserFunction(value = "gs.ecount")
    @Description("Count number of expressions connected to work.")
    public long ecount(
            @Name("node") Node node)
    {
        Iterable<Relationship> wrels = node.getRelationships(RelTypes.REALIZES);

        long counter = 0;
        for (Relationship i : wrels) 
        {
            counter++;
        }
        return counter;
    }

    @UserFunction(value = "gs.mcount")
    @Description("Count number of manifestations connected to work.")
    public long mcount(
            @Name("node") Node node)
    {
        Iterable<Relationship> wrels = node.getRelationships(RelTypes.REALIZES);

        long counter = 0;
        for (Relationship rel : wrels)
        {
            Iterable<Relationship> erels = rel.getOtherNode(node).getRelationships(RelTypes.EMBODIES);
            for (Relationship i : erels) 
            {
                counter++;
            }
        }
        return counter;
    }

    @UserFunction(value = "gs.wmcount")
    @Description("Count number of nodes with properties that match search string.")
    public long wmcount(
            @Name("node") Node node,
            @Name("string") String string)
    {
        ArrayList<NodeLife> currentIteration = new ArrayList<NodeLife>;
        ArrayList<long> nodeIdList = new ArrayList<long>;
        String [] swords = string.split(" ")

        currentIteration.add(node,2,swords);
        nodeIdList.add(node.getId());
        while(currentIteration.size() > 0)
        {
            ArrayList<NodeLife> NextIteration = new ArrayList<NodeLife>;
            for(NodeLife nl : currentIteration)
            {
                Iterable<Relationship> rels = nl.node.getRelationships();
                for(Relationship rel : rels):
                {
                    Node nnode = rel.getOtherNode(nl.node);
                    String words = "";
                    long life = nl.life;

                    if(nnode.hasLabel(Labels.PERSON))
                    {
                        words = nnode.getProperty("name").split(" ");
                    }
                    else
                    {
                        words = nnode.getProperty("title").split(" ");
                    }

                    if(!nodeIdList.contains(nnode.getId()))
                    {
                        nodeIdList.add(nnode.getId());
                    }
                    else
                    {
                        continue;
                    }
                    if(!Collections.disjoint(swords,words))
                    {
                        life = 2;
                    }
                    else 
                    {
                        life--;
                    }
                    if(life > 0)
                    {
                        NextIteration.add(nnode,life,words)
                    }
                }
            }
        }
    }

    private enum RelTypes implements RelationshipType
    {
        REALIZES,
        EMBODIES,
        DERIVATION_OF,
        CONTRIBUTOR_OF
    }

    private enum Labels implements Label
    {
        PERSON
    }
}
