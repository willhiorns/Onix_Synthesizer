
package com.onix.synthesizer;

import StyleResources.Colors;
import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.devices.AudioDeviceManager;
import com.jsyn.unitgen.LineOut;
import java.awt.Color;
import java.io.IOException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import onix.Modules.AmpEnvelopeModule;
import onix.Modules.FilterModule;
import onix.Modules.LfoModule;
import onix.Modules.LogoModule;
import onix.Modules.Osc1Module;
import onix.Modules.OscilloscopeModule;
import onix.Modules.PolyphonyModule;
import onix.Modules.WhiteNoiseModule;
import onix.funcional.modules.MidiControl;
import onix.funcional.modules.MidiHandler;



public class MainSynth {
    
JFrame jFrame;
    
JPanel fullPanel,topPanel,downPanel,leftPanel,centerPanel,rightPanel;
    
Synthesizer synth;
LineOut myOut;
    
    MidiHandler midiTest;
    MidiControl midiControl;
    

    void MainSynt() throws InterruptedException, MidiUnavailableException, IOException {
     
        initComoponents(); 
         
    }
            

    private void initComoponents() throws IOException, InterruptedException, MidiUnavailableException  {
       //Init synth components
        myOut = new LineOut();
        synth = JSyn.createSynthesizer();   
       
        jFrame = new JFrame("Onix Synthesizer");
        fullPanel = new JPanel(null);
        topPanel = new JPanel(null);
        downPanel = new JPanel(null);
        leftPanel = new JPanel(null);
        centerPanel = new JPanel(null);
        rightPanel = new JPanel(null);
   
    createPanels();
        
    }
    
    
    private void configSynth (){
        
      int numInputChannels = 2;
      int numOutputChannels = 2;
        
        synth.start( 44100, AudioDeviceManager.USE_DEFAULT_DEVICE, numInputChannels, AudioDeviceManager.USE_DEFAULT_DEVICE,
             numOutputChannels );     
  
        
    }
    
    
    
    private void createModules () throws MidiUnavailableException, IOException,
            InterruptedException {
        
        LogoModule logoModule = new LogoModule(topPanel);
        
        FilterModule filterModule = new FilterModule(downPanel, synth);

        WhiteNoiseModule whiteNoiseModule = new WhiteNoiseModule(downPanel,
                synth,filterModule);      
        
        Osc1Module osc1Module = new Osc1Module(leftPanel, synth, filterModule);    
        
        AmpEnvelopeModule ampEnvelopeModule = new AmpEnvelopeModule(downPanel,
                synth, filterModule,osc1Module);
       
        //Solution for pass the modules instances to the Filter Module
        filterModule.getNoiseModuleInstance(whiteNoiseModule);
        filterModule.getOsc1ModuleInstance(osc1Module);
        
        LfoModule lfoModule = new LfoModule(leftPanel, synth, filterModule,ampEnvelopeModule);
        
        midiControl = new MidiControl();
        midiControl.voicesConfig(osc1Module,synth,lfoModule);
        midiControl.test();
        
        PolyphonyModule polyphonyModule = new PolyphonyModule(topPanel,midiControl);
        
        OscilloscopeModule oscilloscopeModule = new OscilloscopeModule(
                centerPanel,synth,filterModule, whiteNoiseModule);
    }
    
    
    private void createPanels () throws IOException, InterruptedException, MidiUnavailableException {
        
        topPanel.setBackground(Color.BLACK);
        topPanel.setBounds(0, 0,784, 132);
        topPanel.setBorder(BorderFactory.createLineBorder(Colors.REAL_BLACK, 3));   
        
        downPanel.setBackground(Color.BLACK);
        downPanel.setBounds(0, 429,784, 132);
        downPanel.setBorder(BorderFactory.createLineBorder(Colors.REAL_BLACK, 3));
   
        leftPanel.setBackground(Color.BLACK);
        leftPanel.setBounds(0, 132,256, 297);
        leftPanel.setBorder(BorderFactory.createLineBorder(Colors.REAL_BLACK, 3));
 
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setBounds(256, 132,290, 297);
        centerPanel.setBorder(BorderFactory.createLineBorder(Colors.REAL_BLACK, 3));
          
        rightPanel.setBackground(Color.BLACK);
        rightPanel.setBounds(546, 132,238, 297);
        rightPanel.setBorder(BorderFactory.createLineBorder(Colors.REAL_BLACK, 3));
        
        configSynth();
        
        createModules();
         
        fullPanel.add(topPanel);
         fullPanel.add(downPanel);
         fullPanel.add(leftPanel);
         fullPanel.add(centerPanel);
         fullPanel.add(rightPanel);
        
         createMainWindow();
                 
    }
    
    
    private void createMainWindow(){
    
    fullPanel.setBackground(Color.yellow);
    
    jFrame.setBounds(0, 0, 555, 590);   
    jFrame.setVisible(true);
    jFrame.setResizable(false);
    jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE );
    jFrame.add(fullPanel);    
    
    }
    
}
