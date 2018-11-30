package dk.au.itsmap.group4.crispy.database;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import javax.annotation.Nullable;
import androidx.lifecycle.LiveData;

/* Inspired by https://stackoverflow.com/a/50109176 */
public class FSLiveData<T> extends LiveData<T> {

    private ListenerRegistration mListenerRegistration;
    private final DocumentSnapshotListener mListener = new DocumentSnapshotListener();
    private final Class<T> mClassType;
    private final DocumentReference mDocumentRef;

    public FSLiveData(DocumentReference documentRef, Class<T> classType) {
        mDocumentRef = documentRef;
        mClassType = classType;
    }

    @Override
    protected void onActive() {
        super.onActive();
        if(mListenerRegistration == null) {
            mListenerRegistration = mDocumentRef.addSnapshotListener(mListener);
        }
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        mListenerRegistration.remove();
    }

    private class DocumentSnapshotListener implements EventListener<DocumentSnapshot> {

        @Override
        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
            if (e != null) {
                // TODO: Do something on error
                return;
            }
            if (documentSnapshot != null && documentSnapshot.exists()) {
                T document = documentSnapshot.toObject(mClassType);
                setValue(document);
            }

        }
    }
}
