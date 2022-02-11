package com.ahmetkca.engine;

import com.ahmetkca.utils.Observer;
import com.ahmetkca.utils.Subject;

public abstract class Game extends Observer{

    public abstract void init(GameContainer gc);
    public abstract void update(GameContainer gc, float dt);
    public abstract void render(GameContainer gc, Renderer renderer);
    public void setSub(Subject subject) {
        super.setSubject(subject);
    }
}
