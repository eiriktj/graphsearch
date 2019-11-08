package gs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.neo4j.graphdb.Direction;
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

        long count = 0;
        for (Relationship i : wrels) 
        {
            count++;
        }
        return count;
    }

    @UserFunction(value = "gs.mcount")
    @Description("Count number of manifestations connected to work.")
    public long mcount(
            @Name("node") Node node)
    {
        Iterable<Relationship> wrels = node.getRelationships(RelTypes.REALIZES);

        long count = 0;
        for (Relationship rel : wrels)
        {
            Iterable<Relationship> erels = rel.getOtherNode(node).getRelationships(RelTypes.EMBODIES);
            for (Relationship i : erels) 
            {
                count++;
            }
        }
        return count;
    }

    @UserFunction(value = "gs.wmcount")
    @Description("Count number of nodes with properties that match search string.")
    public long wmcount(
            @Name("node") Node node,
            @Name("string") String string)
    {
        ArrayList<Node> searchList = new ArrayList<Node>();
        ArrayList<Long> idList = new ArrayList<Long>();
        ArrayList<Node> currentIteration = new ArrayList<Node>();
        String [] swords = string.split(" ");
        long nodeCount = 0;
        currentIteration.add(node);
        idList.add(node.getId());
        while(!currentIteration.isEmpty())
        {
            searchList.addAll(currentIteration);
            ArrayList<Node> nextIteration = new ArrayList<Node>();
            for(Node n : currentIteration)
            {
                ArrayList<Relationship> rels = new ArrayList<Relationship>();
                n.getRelationships(RelTypes.CONTRIBUTOR_OF, Direction.INCOMING).forEach(rels::add);
                n.getRelationships(RelTypes.DERIVATION_OF, Direction.INCOMING).forEach(rels::add);
                n.getRelationships(RelTypes.REALIZES, Direction.INCOMING).forEach(rels::add);
                n.getRelationships(RelTypes.EMBODIES, Direction.INCOMING).forEach(rels::add);
                for(Relationship rel : rels)
                {
                    if(!idList.contains(rel.getOtherNode(n)))
                    {
                        nextIteration.add(rel.getOtherNode(n));
                        idList.add(n.getId());
                    }
                }
            }
            currentIteration = nextIteration;
        }

        for(Node n : searchList)
        {
            String [] words;
            String property;
            long count = 0;
            if(n.hasLabel(Label.label("Person")))
            {
                property = "name";
            }
            else
            {
                property = "title";
            }
            words = n.getProperty(property).toString().toLowerCase().replaceAll(" [^a-z\\s]","").split(" ");
            for(String s : swords)
            {
                if(Arrays.asList(words).contains(s))
                {
                    count++;
                }
            }
            if(count > 0)
            {
                nodeCount++;
            }
        }

        return nodeCount;
    }

    private enum RelTypes implements RelationshipType
    {
        CONTRIBUTOR_OF,
        DERIVATION_OF,
        EMBODIES,
        REALIZES
    }
}
