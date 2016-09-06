<?xml version="1.0" ?>
<project name="$solution.name" default="build" basedir=".">
	<property environment="env" />
	<property name="jr.charset" value="UTF-8" />
	<property name="jr.root" location="../" />
	<property name="jr.war" value="/var/apps/www/deployment/${solution.id}.war" />
	<property name="jr.sql" value="/var/apps/www/deployment/${solution.id}.sql" />
	<property name="jr.src" value="${Escape.d}{jr.root}/WEB-INF/src" />
	<property name="jr.lib" value="${Escape.d}{jr.root}/WEB-INF/lib" />
	<property name="jr.classes" value="${Escape.d}{jr.root}/WEB-INF/classes" />

	<path id="classpath">
		<fileset dir="${Escape.d}{jr.lib}">
			<include name="**/*.jar" />
		</fileset>
	</path>
	
    <target name="clear">
		<delete dir="${Escape.d}{jr.classes}" />
		<mkdir dir="${Escape.d}{jr.classes}" />
	</target>
	
    <target name="compile" depends="clear">
		<javac debug="true"  destdir="${Escape.d}{jr.classes}" encoding="${Escape.d}{jr.charset}" nowarn="true" includeantruntime="false">
			<src>
				<pathelement location="${Escape.d}{jr.src}" />
			</src>
			<classpath refid="classpath" />
			<compilerarg line="-Xlint:-unchecked -Xlint:-deprecation"/>
			<compilerarg value="-Xlint:none"/>
		</javac>
		<copy todir="${Escape.d}{jr.classes}">
			<fileset dir="${Escape.d}{jr.src}">
				<exclude name="**/*.java" />
			</fileset>
		</copy>
	</target>
	<target name="makewar" depends="compile">
        <war warfile="${Escape.d}{jr.war}" webxml="${Escape.d}{jr.root}/WEB-INF/web.xml">
            <lib dir="${Escape.d}{jr.lib}">
                <include name="**" />
            </lib>
            <fileset dir="${Escape.d}{jr.root}"/>
        </war>
    </target>
	<target name="build">
		<ant target="makewar" />
		<copy tofile="${Escape.d}{jr.sql}" file="${Escape.d}{jr.root}/WEB-INF/${solution.dbFamily}.sql">
	    </copy>
	</target>
</project>