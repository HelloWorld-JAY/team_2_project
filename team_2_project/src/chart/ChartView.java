package chart;

import java.awt.GridLayout;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class ChartView extends JPanel{

	
	JFreeChart chartGender,chartAge,chartCclass;
	ChartPanel chartPanelGender,chartPanelAge,chartPanelCclass;
	ChartGender gender;
	ChartAge	age;
	ChartClass	cclass;
	public ChartView(){
		
		gender = new ChartGender();
		chartGender = gender.getChart(); 
		chartPanelGender=new ChartPanel(chartGender);
		
		age = new ChartAge();
		chartAge = age.getChart(); 
		chartPanelAge=new ChartPanel(chartAge);
		
		cclass = new ChartClass();
		chartCclass = cclass.getChart(); 
		chartPanelCclass=new ChartPanel(chartCclass);
		
		setLayout(new GridLayout(2,2));
		add(chartPanelGender);
		add(chartPanelAge);
		add(chartPanelCclass);
	}
	
	
	
}
