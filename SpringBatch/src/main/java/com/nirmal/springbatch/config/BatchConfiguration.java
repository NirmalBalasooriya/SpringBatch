package com.nirmal.springbatch.config;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.nirmal.springbatch.model.Book;
import com.nirmal.springbatch.processor.BookProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
    private JobBuilderFactory jobBuilderFactory;
     
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
 
    @Autowired
    private org.apache.tomcat.jdbc.pool.DataSource datasource; 
    
    @Bean
    public Job readCSVFilesJob() {
        return jobBuilderFactory
                .get("readCSVFilesJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }
 
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").<Book, Book>chunk(5)
                .reader(reader())
                .processor(new BookProcessor())
                .writer(writer())
                .build();
    }
 
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public FlatFileItemReader<Book> reader()
    {
        FlatFileItemReader<Book> flatFileItemReader = new FlatFileItemReader<Book>();
         
        //Read input file from location
        flatFileItemReader.setResource(new FileSystemResource("input/inputData.csv"));
         
        //Set number of lines to skips. Use it if file has header rows.
        //reader.setLinesToSkip(1);  
         
        //Configure values using mapper
        flatFileItemReader.setLineMapper(new DefaultLineMapper() {
            {
                //3 read each columns from csv
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "isbmNumber", "name", "description", "auther" });
                    }
                });
                //Set values in Book class
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Book>() {
                    {
                        setTargetType(Book.class);
                    }
                });
            }
        });
        return flatFileItemReader;
    }
     
    /**
     * JDBCBatch Item writer
     * @return
     */
    @Bean
    public JdbcBatchItemWriter<Book> writer() {
        JdbcBatchItemWriter<Book> itemWriter = new JdbcBatchItemWriter<Book>();
        itemWriter.setDataSource(datasource);
        itemWriter.setSql("INSERT INTO BOOK (ISBMNUMBER, NAME, DESCRIPTION, AUTHER) VALUES (:isbmNumber, :name, :description, :auther)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Book>());
        return itemWriter;
    }

}