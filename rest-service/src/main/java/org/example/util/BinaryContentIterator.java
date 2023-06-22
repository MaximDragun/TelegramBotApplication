package org.example.util;

import org.example.models.BinaryContent;
import org.example.models.enums.TypeFile;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Iterator;

@Transactional(readOnly = true)
@Component
public class BinaryContentIterator {

    public Iterator<BinaryContent> iterateBinaryContents(EntityManager entityManager, Long applicationDocumentId, TypeFile file_type) {
        Session session = entityManager.unwrap(Session.class);
        ScrollableResults results = session.createQuery("SELECT bc FROM BinaryContent bc WHERE bc.fileId = :id and bc.typeFile = :file_type")
                .setParameter("id", applicationDocumentId)
                .setParameter("file_type", file_type)
                .scroll(ScrollMode.FORWARD_ONLY);

        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                boolean hasNext = results.next();
                if (!hasNext) {
                    closeSessionAndResults();
                }
                return hasNext;
            }

            @Override
            public BinaryContent next() {
                return (BinaryContent) results.get()[0];
            }

            private void closeSessionAndResults() {
                results.close();
                session.close();
            }
        };
    }
}
