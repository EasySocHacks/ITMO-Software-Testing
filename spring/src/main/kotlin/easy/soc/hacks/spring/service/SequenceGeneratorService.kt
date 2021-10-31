package easy.soc.hacks.spring.service

import easy.soc.hacks.spring.domain.DatabaseSequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.FindAndModifyOptions.options
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service


@Service
class SequenceGeneratorService(
    @Autowired private val mongo: MongoOperations
) {

    fun generateSequence(seqName: String): Long {
        val counter: DatabaseSequence? = mongo.findAndModify(
            query(where("_id").`is`(seqName)),
            Update().inc("seq", 1), options().returnNew(true).upsert(true),
            DatabaseSequence::class.java
        )

        return when (counter) {
            null -> 1
            else -> counter.seq
        }
    }
}