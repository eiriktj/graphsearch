package gs;

import org.neo4j.graphdb.Node;
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
        for (Object i : wrels) 
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
            for (Object i : erels) 
            {
                counter++;
            }
        }
        return counter;
    }

    private enum RelTypes implements RelationshipType
    {
        REALIZES,
        EMBODIES
    }
}
