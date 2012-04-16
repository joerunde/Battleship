import com.sun.j3d.utils.picking.*;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.ViewingPlatform;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.*;

import javax.media.j3d.*;

import javax.vecmath.*;

import java.awt.event.*;

import java.awt.*;
import java.io.FileNotFoundException;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;

public class BattleshipGui3D extends MouseAdapter implements BattleshipGui{

	private PickCanvas pickCanvas;
	private SimpleUniverse universe;
	private BranchGroup group;
	private Canvas3D canvas;
	private TransformGroup buttonTfs[][], shipTfs[], waveTf;
	private Scene buttonScene[][], shipScene[], waveScene;
	private Transform3D buttonTransform, shipTransform, waveTransform;
	private DirectionalLight buttonLights[][], shipLights[], waveLight;
	private ObjectFile bfiles[][], shipFiles[], waveFile;
	
	private static final int FRAME_WIDTH = 1055;
	private static final int FRAME_HEIGHT = 585;
	private int buttonFired;
	private Color3f gray, red, blue, lblue, green, black;
	
	public BattleshipGui3D()
	{
		
	    Frame frame = new Frame("Box and Sphere");

	    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

	    canvas = new Canvas3D(config);

	    canvas.setSize(FRAME_WIDTH, FRAME_HEIGHT);

	    group = new BranchGroup();
	    buttonTfs = new TransformGroup[2][100];
	    shipTfs = new TransformGroup[5];
	    buttonScene = new Scene[2][100];
	    shipScene = new Scene[5];
	    bfiles = new ObjectFile[2][100];
	    shipFiles = new ObjectFile[5];
	    buttonTransform = new Transform3D();
	    shipTransform = new Transform3D();
	    waveTransform = new Transform3D();
	    
	    Vector3f lightDirection = new Vector3f(-1F, -1F, -1F);
	    buttonLights = new DirectionalLight[2][100];
	    shipLights = new DirectionalLight[5];
	    gray = new Color3f(1,1,1);
	    red = new Color3f(3,0,0);
	    blue = new Color3f(0,0,3);
	    green = new Color3f(0,3,0);
	    black = new Color3f(.3f,.3f,.3f);
	    lblue = new Color3f(.5f,.5f,3);

	    Point3d pointlol = new Point3d(0,0,0);
	    Bounds lightBounds = new BoundingSphere(pointlol, .001);
	    
	    for(int k = 0; k < 2; k++)
			for(int c = 0;c < 100; c++)
			{
				bfiles[k][c] = new ObjectFile(ObjectFile.RESIZE);
				
					try {
						buttonScene[k][c] = bfiles[k][c].load("button.obj");
					} catch (FileNotFoundException e) {
						System.out.println("notfound\n");
					} catch (IncorrectFormatException e) {
						System.out.println("format\n");
					} catch (ParsingErrorException e) {
						System.out.println("parsing\n");
					}
					buttonLights[k][c] = new DirectionalLight(gray,lightDirection);
					buttonLights[k][c].setCapability(DirectionalLight.ALLOW_COLOR_WRITE);
					buttonLights[k][c].setInfluencingBounds(lightBounds);
					buttonScene[k][c].getSceneGroup().addChild(buttonLights[k][c]);
					buttonTfs[k][c] = new TransformGroup(buttonTransform);	    	
			}
	    
	    for(int c = 0; c < 5; c++)
	    	shipFiles[c] = new ObjectFile(ObjectFile.RESIZE);
	    waveFile = new ObjectFile(ObjectFile.RESIZE);
	    
	    try{
	    	shipScene[0] = shipFiles[0].load("carrier.obj");
	    	shipScene[1] = shipFiles[1].load("battleship.obj");
	    	shipScene[2] = shipFiles[2].load("submarine.obj");
	    	shipScene[3] = shipFiles[3].load("submarine.obj");
	    	shipScene[4] = shipFiles[4].load("frigate.obj");
	    	waveScene = waveFile.load("waves.obj");
	    } catch (FileNotFoundException e) {
			System.out.println("ship notfound\n");
		} catch (IncorrectFormatException e) {
			System.out.println("ship format\n");
		} catch (ParsingErrorException e) {
			System.out.println("ship parsing\n");
		}
	    
	    for(int c = 0; c < 5; c++){
	    	shipLights[c] = new DirectionalLight(black,lightDirection);
	    	shipLights[c].setCapability(DirectionalLight.ALLOW_COLOR_WRITE);
	    	shipLights[c].setCapability(DirectionalLight.ALLOW_INFLUENCING_BOUNDS_WRITE);
	    	shipLights[c].setCapability(DirectionalLight.ALLOW_BOUNDS_WRITE);
	    	shipLights[c].setInfluencingBounds(lightBounds);
	    	shipScene[c].getSceneGroup().addChild(shipLights[c]);
	    	shipTfs[c] = new TransformGroup(shipTransform);
	    }
	    
	    lightDirection = new Vector3f(1,-2,1);
	    waveLight = new DirectionalLight(lblue,lightDirection);
	    waveLight.setInfluencingBounds(lightBounds);
	    waveScene.getSceneGroup().addChild(waveLight);
	    waveTf = new TransformGroup(shipTransform);
	    
	    for(int k = 0; k < 2; k++)
	    	for(int i = 0; i < 10; i++)
	    		for(int c = 0; c < 10; c++)
	    		{
	    			
	    			int buttonID = 10*c+i;
	    			Vector3d buttonTran = new Vector3d(i*.50 -5.10 + k*5.10, 0, c*0.50+0.20);
	    			buttonTransform.setScale(.2);
	    			buttonTransform.setTranslation(buttonTran);
	    			buttonScene[k][buttonID].getSceneGroup().getChild(0).setName(""+buttonID);
	    			buttonTfs[k][buttonID].addChild(buttonScene[k][buttonID].getSceneGroup());
	    			buttonTfs[k][buttonID].setTransform(buttonTransform);
	    			buttonTfs[k][buttonID].setCapability(TransformGroup.ALLOW_PICKABLE_WRITE);
	    			group.addChild(buttonTfs[k][buttonID]);
	    		}
	    
	    for(int c = 0; c < 5; c++){
	    	Vector3d shipTran = new Vector3d(0,c*.5f,-1);
	    	if(c == 2 || c == 3)
	    		shipTransform.setScale(.6);
	    	if(c == 1);
	    		shipTransform.setScale(.8);
	    	if(c == 4)
	    		shipTransform.setScale(.5);
	    	shipTransform.setTranslation(shipTran);
	    	shipTfs[c].addChild(shipScene[c].getSceneGroup());
	    	shipTfs[c].setPickable(false);
	    	shipTfs[c].setTransform(shipTransform);
	    	shipTfs[c].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    	group.addChild(shipTfs[c]);
	    }
	    
	    waveTransform.setScale(12);
	    waveTransform.setTranslation(new Vector3d(0,-1,1));
	    waveTf.addChild(waveScene.getSceneGroup());
	    waveTf.setPickable(false);
	    waveTf.setTransform(waveTransform);
	    group.addChild(waveTf);
	 
	    ViewingPlatform ourView;
	    ourView = new ViewingPlatform();
	    ourView.getViewPlatform().setActivationRadius(3000f);
	    
	    TransformGroup viewTransform = ourView.getViewPlatformTransform();
	    Transform3D t3d = new Transform3D();
	    t3d.lookAt(new Point3d(-0.25f,7.f,13.9f), new Point3d(-0.25,0,2.f), new Vector3d(0,1,0));
	    t3d.invert();
	    viewTransform.setTransform(t3d);
	    
	    Viewer viewer = new Viewer(canvas);
	    View view = viewer.getView();
	    view.setBackClipDistance(3000);

	    universe = new SimpleUniverse(ourView, viewer);

	    
	    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);	    
	    BranchGroup bgGroup = new BranchGroup();
	    Sphere outerWorld = new Sphere( 1.0f, Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS_INWARD, 1);
	    bgGroup.addChild(outerWorld); 
	    Background bg = new Background();
	    bg.setApplicationBounds(bounds);
	    bg.setGeometry(bgGroup);
	    group.addChild(bg);
	    
	    ///////////////////////////////////////////////////////////////////////////////////////
	    
	    BoundingSphere bounds5 = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
	    OrbitBehavior B = new OrbitBehavior(canvas);
	    B.setSchedulingBounds(bounds5);
	    ourView.setViewPlatformBehavior(B);
	    
	    GeometryInfo lolwaves = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
	    Point3d wavepoints[] = new Point3d[100];
	    
	    int striplol[] = new int[10];
	    for(int c = 0; c < 10; c++)
	    	striplol[c] = 10;
	    
	    
	    for(int i = 0; i < 10; i++)
	    	for(int c = 0; c < 10; c++)
	    	{
	    		int ID = i*10 + c;
	    		wavepoints[ID] = new Point3d();
	    		wavepoints[ID].setX(c);
	    		wavepoints[ID].setY(i);
	    		wavepoints[ID].setZ(0);
	    	}
	    lolwaves.setCoordinates(wavepoints);
	    lolwaves.setStripCounts(striplol);
	    
	    Triangulator tr = new Triangulator();
	    tr.triangulate(lolwaves);
	    NormalGenerator ng = new NormalGenerator();
	    ng.generateNormals(lolwaves);
	    Stripifier st = new Stripifier();
	    st.stripify(lolwaves);
	    Shape3D wavelol = new Shape3D();
	    wavelol.setGeometry(lolwaves.getGeometryArray());
	    
	    group.addChild(wavelol);
	    group.addChild(new Shape3D(lolwaves.getGeometryArray()));
	    
	    
	    float height = 0.5f;
	    float radius = 0.1f;
	    int verticesNum = 40;
	    float[] vertices = new float[verticesNum * 3];

	    float perR = (float) (2 * Math.PI / (verticesNum / 2 - 1));
	    /*for (int i = 0; i < verticesNum; i += 2) {
	      vertices[(i + 1) * 3] = vertices[i * 3] = (float) (radius * Math.cos(i / 2 * perR));
	      vertices[(i + 1) * 3 + 2] = vertices[i * 3 + 2] = (float) (radius * Math.sin(i / 2 * perR));
	      vertices[i * 3 + 1] = height;
	      vertices[(i + 1) * 3 + 1] = 0;
	    }*/
	    for(int c = 0; c < 5; c++)
	    	for(int i = 0; i < 24; i+= 3)
	    	{  
	    		int ID = c*24 + i;
	    		vertices[ID] = i;
	    		vertices[ID + 2] = i;
	    		vertices[ID + 1] = i;
	    		System.out.println(ID);
	    	}

	    TriangleStripArray quadArray = new TriangleStripArray(verticesNum,
	        GeometryArray.COORDINATES,new int[] { verticesNum });
	    quadArray.setCoordinates(0, vertices);
	    
	    //Shape3D waves = new Shape3D(quadArray);
	    
	    //group.addChild(waves);
	    group.addChild(new Shape3D(quadArray));
	    
	    
	    //////////////////////////////////////////////////////////////////////////////////////////
	    
	    
	    universe.addBranchGraph(group);
	    
	    frame.addWindowListener(new WindowAdapter() {

	       public void windowClosing(WindowEvent winEvent) {

	          System.exit(0);

	       }

	    });

	    frame.add(canvas);

	    pickCanvas = new PickCanvas(canvas, group);

	    pickCanvas.setMode(PickCanvas.GEOMETRY);

	    canvas.addMouseListener(this);

	    frame.pack();

	    frame.show();
	    
	    //buttonLights[0][50].setColor(blue);
	}
	
	
	public void setMessage(String message){
		//winnerLabel.setText(message);
	}
	
	public void setupBoard(BattleshipBoard board){
		int c, i;
		for(i = 0; i < 10; i++)
			for(c = 0; c < 10; c++)
				if(board.isOccupied(c, i))
					updateButton(c,i,2,0);
	}
	
	public void setButtonFired(int button){
		this.buttonFired = button;
	}
	
	public int getButtonFired(){
		return this.buttonFired;
	}
	
	public void setButtonsEnabled(boolean enabled, int side){
		int c;
		for(c = 0; c < 100; c++)
			buttonTfs[side][c].setPickable(enabled);
	}
	
	public void updateButton(int row, int col, int updateID, int boardID){
		int c = 10*col + row;
		
				if(updateID == 0){
					buttonLights[boardID][c].setColor(blue);
				}
				if(updateID == 1){
					buttonLights[boardID][c].setColor(red);
				}
				if(updateID == 2){
					buttonLights[boardID][c].setColor(green);
				}
				if(updateID == 3){
					buttonLights[boardID][c].setColor(gray);
				}
	}
	
	public void placeShip(int col, int row, boolean rotate, int ship){
		Matrix3d rotmat = new Matrix3d(0,0,1,0,1,0,1,0,0);
		Matrix3d idmat = new Matrix3d(1,0,0,0,1,0,0,0,1);
		Vector3d shipTran = new Vector3d();
		if(ship == 0){
			System.out.println("carrier");
			shipTransform.setScale(1.1);
			if(!rotate)
				shipTran = new Vector3d(.5 * col - 4.1, .4, .5 * row + .2);
			else
				shipTran = new Vector3d(.5 * col - 5.2, .4, .5 * row + 1.3);
		}
		if(ship == 1){
			System.out.println("battleship");
			shipTransform.setScale(0.9);
			if(!rotate)
				shipTran = new Vector3d(.5 * col - 4.3, .4, .5 * row + .3);
			else
				shipTran = new Vector3d(.5 * col - 5.1, .4, .5 * row + 1.0);
		}
		if(ship == 2 || ship == 3){
			System.out.println("sub");
			shipTransform.setScale(0.6);
			if(!rotate)
				shipTran = new Vector3d(.5 * col - 4.6, .2, .5 * row + .2);
			else
				shipTran = new Vector3d(.5 * col - 5.1, .2, .5 * row + .7);
		}
		if(ship == 4){
			System.out.println("frigate");
			shipTransform.setScale(0.5);
			if(!rotate)
				shipTran = new Vector3d(.5 * col - 4.8, .3, .5 * row + .3);
			else
				shipTran = new Vector3d(.5 * col - 5.1, .3, .5 * row + .5);
		}
		if(rotate)
			shipTransform.setRotation(rotmat);
		else
			shipTransform.setRotation(idmat);
		shipTransform.setTranslation(shipTran);
		shipTfs[ship].setTransform(shipTransform);
		shipLights[ship].setColor(blue);
		
		Point3d pointlol = new Point3d(0,0,0);
	    Bounds lightBounds = new BoundingSphere(pointlol, 1);
	    //shipLights[ship].setInfluencingBounds(lightBounds);
	    //shipLights[ship].setBounds(lightBounds);
		//shipLights[ship].setBoundsAutoCompute(true);
	}
	
	public void setVisible(boolean visible){}
	
	public void mouseClicked(MouseEvent e)
	{
	    pickCanvas.setShapeLocation(e);
	    PickResult result = pickCanvas.pickClosest();

	    if (result == null) {
	       System.out.println("Nothing picked");
	    } else {
	       Primitive p = (Primitive)result.getNode(PickResult.PRIMITIVE);
	       Shape3D s = (Shape3D)result.getNode(PickResult.SHAPE3D);
	       if (p != null) {
	          //do nothing
	       } else if (s != null) {
	             //System.out.println(s.getName());
	             setButtonFired(Integer.parseInt(s.getName()));
	             //System.out.println(s.getClass().getName());
	       } else{
	          System.out.println("null");
	       }

	    }

	}
	
	
	
	
	
	
}