package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.EbookSnapshot;
import com.blog.mapper.EbookSnapshotMapper;
import com.blog.service.EbookSnapshotService;
import org.springframework.stereotype.Service;

@Service
public class EbookSnapshotServiceImpl extends ServiceImpl<EbookSnapshotMapper, EbookSnapshot> implements EbookSnapshotService {
}
