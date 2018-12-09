package dk.au.itsmap.group4.crispy.database;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import androidx.lifecycle.LiveData;
import dk.au.itsmap.group4.crispy.database.entity.Entity;

/* Inspired by https://stackoverflow.com/a/50109176 */
public class FSCollectionLiveData<T extends U, U> extends LiveData<List<U>> {

    private ListenerRegistration mListenerRegistration;
    private final QuerySnapshotListener mListener = new QuerySnapshotListener();
    private final Class<T> mClassType;
    private final Query mQuery;

    FSCollectionLiveData(Query query, Class<T> classType) {
        mQuery = query;
        mClassType = classType;
    }

    @Override
    protected void onActive() {
        super.onActive();
        mListenerRegistration = mQuery.addSnapshotListener(mListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if(mListenerRegistration != null) {
            mListenerRegistration.remove();
        }
    }

    private class QuerySnapshotListener implements EventListener<QuerySnapshot> {

        @Override
        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
            if (e != null) {
                // TODO: Do something on error
                return;
            }
            if (queryDocumentSnapshots != null) {
                List<U> documents = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    T fetchedDocument = doc.toObject(mClassType);
                    if(Entity.class.isAssignableFrom(mClassType))
                        ((Entity) fetchedDocument).setId(doc.getId());
                    documents.add(fetchedDocument);
                }
                setValue(documents);
            }
        }
    }
}
