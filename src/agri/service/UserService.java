package agri.service;

import java.util.List;

import org.slim3.datastore.Datastore;

import agri.meta.UserMeta;
import agri.model.User;
import agri.model.Yago;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class UserService{

    private UserMeta meta = UserMeta.get();
    
    public User get(Key key, Long version) {
        return Datastore.get(meta,key,version);
    }
    
    public User get(Key key) {
        return Datastore.get(meta, key);
    }
    
    public User getByEmail(String email) {
        User user = Datastore.query(meta).filter(
            meta.email.equal(new Email(email))).asSingle();
        return user;
    }
    
    public User getByEmail2(String email2) {
        if(Util.isEmpty(email2)) return null;
        User user = Datastore.query(meta).filter(
            meta.email2.equal(new Email(email2))).asSingle();
        return user;
    }
    
    public List<User> getKakudukeTantoList(Yago yago) {
        return Datastore.query(meta).filter(meta.yagoRef.equal(yago.getKey()),
            meta.kakudukeTanto.equal(true), meta.delete.equal(false)).sort(meta.name.asc).asList();
    }
    
    public List<User> getAll(Yago yago) {
        
        return yago.getUserListWithOutDeleted();
    }
    
    public List<User> getAll() {
        return Datastore.query(meta).filter(
            meta.delete.equal(false)).sort(meta.name.asc).asList();
    }
    
    public List<User> getAllWithDeleted(Yago yago) {
        return yago.getUserListRef().getModelList();
    }
    
    public List<User> getUseDakokuList(Yago yago) {
        return Datastore.query(meta).filter(meta.yagoRef.equal(yago.getKey()),
            meta.useDakoku.equal(true), meta.delete.equal(false)).sort(meta.name.asc).asList();
    }

    public void insert(User user) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, user);
        tx.commit();
    }
    
    public void update(User user) {
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(tx, user);
        tx.commit();
    }
    
    public void delete(Key key, Long version) {

        Transaction tx = Datastore.beginTransaction();
        User user = get(key, version);
        if(user.getSagyoUserListRef().getModelList().size() > 0
                || user.getSakudukeListRef().getModelList().size() > 0) {
            user.setDelete(true);
            Datastore.put(user);
        } else {
            Datastore.delete(key);
        }
        
        tx.commit();
    }
}
